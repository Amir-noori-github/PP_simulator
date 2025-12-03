package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;
import simu.framework.IEventType;
import simu.framework.Trace;

import java.util.LinkedList;
import java.util.Queue;

public class ServicePoint {
    private ContinuousGenerator generator;
    private EventList eventList;
    private IEventType departureType;

    private Queue<Visitor> queue = new LinkedList<>();
    private Visitor currentVisitor;
    private boolean reserved = false;

    // Stats
    private int servedCount = 0;
    private double busyTime = 0.0;

    private static final double EPS = 1e-9;

    public ServicePoint(ContinuousGenerator generator, EventList eventList, IEventType departureType) {
        this.generator = generator;
        this.eventList = eventList;
        this.departureType = departureType;
    }

    /** Add a visitor to the queue */
    public void addQueue(Visitor v) {
        queue.add(v);
        Trace.out(Trace.Level.INFO, "Visitor " + v.getId() + " added to queue at " + departureType);
    }

    /** Begin service for the next visitor in queue */
    public void beginService() {
        if (!reserved && !queue.isEmpty()) {
            currentVisitor = queue.poll();
            reserved = true;

            double serviceTime = generator.sample();
            if (serviceTime <= 0) serviceTime = EPS;

            double departureTime = Clock.getInstance().getClock() + serviceTime;
            busyTime += serviceTime;

            Event e = new Event(departureType, departureTime, currentVisitor);
            eventList.add(e);

            Trace.out(Trace.Level.INFO,
                    "ServicePoint " + departureType +
                            " begins service for Visitor " + currentVisitor.getId() +
                            " -> departure scheduled at " + departureTime);
        }
    }

    /** Finish service for the current visitor */
    public Visitor endService() {
        reserved = false;
        Visitor finished = currentVisitor;
        currentVisitor = null;
        servedCount++;
        return finished;
    }

    public boolean isOnQueue() {
        return !queue.isEmpty();
    }

    public boolean isReserved() {
        return reserved;
    }

    public double getUtilization() {
        double now = Clock.getInstance().getClock();
        return now > 0 ? busyTime / now : 0.0;
    }

    public int getThroughput() {
        return servedCount;
    }
}
