package simu.framework;

import java.util.PriorityQueue;

/**
 * EventList holds events ordered by their scheduled time.
 * The event with the smallest time will be retrieved first.
 */
public class EventList {
    private PriorityQueue<Event> eventlist;

    public EventList() {
        eventlist = new PriorityQueue<>();
    }

    /**
     * Retrieve and remove the next event from the list.
     *
     * @return The next event, or null if the list is empty
     */
    public Event remove() {
        if (eventlist.isEmpty()) {
            Trace.out(Trace.Level.INFO, "Event list empty, nothing to remove");
            return null;
        }
        Event next = eventlist.remove();
        Trace.out(Trace.Level.INFO,
                "Removing event: " + next.getType() +
                        " at time " + next.getTime() +
                        (next.getVisitor() != null ? " for Visitor " + next.getVisitor().getId() : ""));
        return next;
    }

    /**
     * Add a new event to the list.
     *
     * @param e Event to be inserted
     */
    public void add(Event e) {
        eventlist.add(e);
        Trace.out(Trace.Level.INFO,
                "Adding event: " + e.getType() +
                        " at time " + e.getTime() +
                        (e.getVisitor() != null ? " for Visitor " + e.getVisitor().getId() : ""));
    }

    /**
     * Check the time of the next event without removing it.
     *
     * @return Time of the next event, or Double.NaN if list is empty
     */
    public double getNextEventTime() {
        if (eventlist.isEmpty()) return Double.NaN;
        return eventlist.peek().getTime();
    }

    /**
     * Peek at the next event without removing it.
     *
     * @return The next event, or null if list is empty
     */
    public Event peek() {
        return eventlist.peek();
    }

    /**
     * Check if the event list is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return eventlist.isEmpty();
    }
}