package simu.model;

import simu.framework.*;
import java.util.*;

/**
 * Visitor in the amusement park simulator.
 * Tracks arrival/departure times per attraction using EventType.
 */
public class Visitor {
    private double arrivalTime;
    private double removalTime;
    private int id;
    private static int counter = 1;
    private static double totalSystemTime = 0;

    private List<EventType> visitedAttractions = new ArrayList<>();
    private Map<EventType, Double> attractionArrivalTimes = new HashMap<>();
    private Map<EventType, Double> attractionDepartureTimes = new HashMap<>();

    private String currentLocation;
    private String nextDestination;

    private double totalWaitTime = 0;
    private double totalServiceTime = 0;

    public Visitor() {
        id = counter++;
        arrivalTime = Clock.getInstance().getClock();
        Trace.out(Trace.Level.INFO, "New visitor #" + id + " entered park at " + arrivalTime);
    }

    public double getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(double arrivalTime) { this.arrivalTime = arrivalTime; }

    public double getRemovalTime() { return removalTime; }
    public void setRemovalTime(double removalTime) { this.removalTime = removalTime; }

    public int getId() { return id; }

    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String location) { this.currentLocation = location; }

    public String getNextDestination() { return nextDestination; }
    public void setNextDestination(String destination) { this.nextDestination = destination; }

    public void arriveAtAttraction(EventType type) {
        visitedAttractions.add(type);
        attractionArrivalTimes.put(type, Clock.getInstance().getClock());
        Trace.out(Trace.Level.INFO, "Visitor #" + id + " arrived at " + type);
    }

    public void departFromAttraction(EventType type) {
        attractionDepartureTimes.put(type, Clock.getInstance().getClock());
        Trace.out(Trace.Level.INFO, "Visitor #" + id + " departed from " + type);
    }

    public double getTotalSystemTime() {
        return removalTime - arrivalTime;
    }

    public void addWaitTime(double wait) {
        totalWaitTime += wait;
    }

    public double getTotalWaitTime() {
        return totalWaitTime;
    }

    public double getTotalServiceTime() {
        return totalServiceTime;
    }

    public void reportResults() {
        Trace.out(Trace.Level.INFO, "\nVisitor #" + id + " completed journey.");
        Trace.out(Trace.Level.INFO, "Entered park: " + arrivalTime);
        Trace.out(Trace.Level.INFO, "Exited park: " + removalTime);
        Trace.out(Trace.Level.INFO, "Total time in system: " + getTotalSystemTime());
        Trace.out(Trace.Level.INFO, "Visited attractions: " + visitedAttractions);

        for (EventType type : visitedAttractions) {
            double in = attractionArrivalTimes.getOrDefault(type, -1.0);
            double out = attractionDepartureTimes.getOrDefault(type, -1.0);
            if (in >= 0 && out >= 0) {
                Trace.out(Trace.Level.INFO, " - " + type + ": " + (out - in) + " minutes");
            }
        }

        totalSystemTime += getTotalSystemTime();
        double mean = totalSystemTime / id;
        System.out.println("Current mean system time across visitors: " + mean);
    }
}
