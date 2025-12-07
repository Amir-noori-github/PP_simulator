package simu.model;

import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import simu.framework.*;

import java.util.Random;

/**
 * Main simulator engine for the amusement park.
 * Implements the three-phase simulation loop with branching paths.
 */
public class MyEngine extends Engine {
    private ArrivalProcess ticketBoothArrivals;
    private ServicePoint ticketBooth, restArea, attraction1, attraction2, attraction3;

    public MyEngine(double arrivalMean,
                    double ticketMean, double ticketStd,
                    double restMean, double restStd,
                    double attr1Mean, double attr1Std,
                    double attr2Mean, double attr2Std,
                    double attr3Mean, double attr3Std) {

        Random r = new Random();

        // Arrival process: exponential arrivals to ticket booth
        ticketBoothArrivals = new ArrivalProcess(
                new Negexp(arrivalMean, Integer.toUnsignedLong(r.nextInt())),
                eventList,
                EventType.ARRIVAL_TICKETBOOTH
        );

        // Service points with configurable Normal service times
        ticketBooth = new ServicePoint(new Normal(ticketMean, ticketStd), eventList, EventType.DEPARTURE_TICKETBOOTH);
        restArea   = new ServicePoint(new Normal(restMean,  restStd),  eventList, EventType.DEPARTURE_REST);
        attraction1 = new ServicePoint(new Normal(attr1Mean, attr1Std), eventList, EventType.DEPARTURE_ATTRACTION1);
        attraction2 = new ServicePoint(new Normal(attr2Mean, attr2Std), eventList, EventType.DEPARTURE_ATTRACTION2);
        attraction3 = new ServicePoint(new Normal(attr3Mean, attr3Std), eventList, EventType.DEPARTURE_ATTRACTION3);
    }

    @Override
    protected void initialize() {
        // Schedule the first arrival
        ticketBoothArrivals.generateNextEvent();
    }

    @Override
    protected void runEvent(Event e) {
        Visitor v = e.getVisitor();

        switch ((EventType) e.getType()) {
            case ARRIVAL_TICKETBOOTH:
                ticketBooth.addQueue(v);
                v.arriveAtAttraction("TicketBooth");
                ticketBoothArrivals.generateNextEvent();
                break;

            case DEPARTURE_TICKETBOOTH:
                v = ticketBooth.endService();
                v.departFromAttraction("TicketBooth", 0);
                restArea.addQueue(v);
                v.arriveAtAttraction("RestArea");
                break;

            case DEPARTURE_REST:
                v = restArea.endService();
                v.departFromAttraction("RestArea", 0);
                double rnd = Math.random();
                if (rnd < 0.33) {
                    v.arriveAtAttraction("Attraction1");
                    attraction1.addQueue(v);
                } else if (rnd < 0.66) {
                    v.arriveAtAttraction("Attraction2");
                    attraction2.addQueue(v);
                } else {
                    v.arriveAtAttraction("Attraction3");
                    attraction3.addQueue(v);
                }
                break;

            case DEPARTURE_ATTRACTION1:
                v = attraction1.endService();
                v.departFromAttraction("Attraction1", 0);
                handleExitOrReturn(v);
                break;

            case DEPARTURE_ATTRACTION2:
                v = attraction2.endService();
                v.departFromAttraction("Attraction2", 0);
                handleExitOrReturn(v);
                break;

            case DEPARTURE_ATTRACTION3:
                v = attraction3.endService();
                v.departFromAttraction("Attraction3", 0);
                handleExitOrReturn(v);
                break;

            default:
                break;
        }
    }

    /**
     * Decide whether visitor exits the park or returns to Rest Area.
     */
    private void handleExitOrReturn(Visitor v) {
        if (Math.random() < 0.3) { // 30% chance to exit
            v.setRemovalTime(Clock.getInstance().getClock());
            v.reportResults();
            Trace.out(Trace.Level.INFO, "Visitor " + v.getId() + " exited at " + Clock.getInstance().getClock());
        } else {
            restArea.addQueue(v);
            v.arriveAtAttraction("RestArea");
        }
    }

    @Override
    protected void tryCEvents() {
        if (!ticketBooth.isReserved() && ticketBooth.isOnQueue()) ticketBooth.beginService();
        if (!restArea.isReserved()    && restArea.isOnQueue())    restArea.beginService();
        if (!attraction1.isReserved() && attraction1.isOnQueue()) attraction1.beginService();
        if (!attraction2.isReserved() && attraction2.isOnQueue()) attraction2.beginService();
        if (!attraction3.isReserved() && attraction3.isOnQueue()) attraction3.beginService();
    }

    @Override
    protected void results() {
        System.out.println("Simulation ended at " + Clock.getInstance().getClock());
        System.out.println("Ticket Booth utilization: " + ticketBooth.getUtilization());
        System.out.println("Rest Area utilization: " + restArea.getUtilization());
        System.out.println("Attraction 1 throughput: " + attraction1.getThroughput());
        System.out.println("Attraction 2 throughput: " + attraction2.getThroughput());
        System.out.println("Attraction 3 throughput: " + attraction3.getThroughput());
    }
}