package simu.framework;

import eduni.distributions.ContinuousGenerator;
import simu.model.Visitor;

public class ArrivalProcess {
    private ContinuousGenerator generator;
    private EventList eventList;
    private IEventType type;

    private static final double EPS = 1e-9;

    public ArrivalProcess(ContinuousGenerator g, EventList tl, IEventType type) {
        this.generator = g;
        this.eventList = tl;
        this.type = type;
    }

    /** Generate the next visitor arrival event */
    public void generateNextEvent() {
        double dt = generator.sample();
        if (dt <= 0) dt = EPS;

        double nextTime = Clock.getInstance().getClock() + dt;

        Visitor v = new Visitor();
        Event e = new Event(type, nextTime, v);
        eventList.add(e);

        Trace.out(Trace.Level.INFO,
                "Scheduled arrival event at time " + nextTime +
                        " for Visitor " + v.getId());
    }
}
