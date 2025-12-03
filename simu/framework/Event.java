package simu.framework;

import simu.model.Visitor;

/**
 * Event holds three-phase simulation event information:
 * - type of the event
 * - time of the event
 * - associated visitor
 *
 * Events are compared according to time.
 */
public class Event implements Comparable<Event> {
    private IEventType type;
    private double time;
    private Visitor visitor; // link to the visitor involved in this event

    public Event(IEventType type, double time) {
        this.type = type;
        this.time = time;
    }

    public Event(IEventType type, double time, Visitor visitor) {
        this.type = type;
        this.time = time;
        this.visitor = visitor;
    }

    public void setType(IEventType type) {
        this.type = type;
    }

    public IEventType getType() {
        return type;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    @Override
    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }
}