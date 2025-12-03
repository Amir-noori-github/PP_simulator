package simu.model;

import simu.framework.IEventType;

/**
 * Enumeration of all event types in the amusement park simulator.
 * Implements IEventType so the framework can handle them generically.
 */
public enum EventType implements IEventType {
    ARRIVAL_TICKETBOOTH,
    DEPARTURE_TICKETBOOTH,
    DEPARTURE_REST,
    DEPARTURE_ATTRACTION1,
    DEPARTURE_ATTRACTION2,
    DEPARTURE_ATTRACTION3,
    EXIT_PARK
}
