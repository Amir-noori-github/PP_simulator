package simu.framework;

/**
 * Engine implements a three-phase simulator.
 * See <a href="https://www.jstor.org/stable/2584330">Three-Phase Simulator</a>
 *
 * This is a skeleton of a three-phase simulator. Subclasses must implement
 * the abstract methods for their specific simulation model.
 */
public abstract class Engine {
    private double simulationTime = 0;   // time when the simulation will be stopped
    private Clock clock;                 // shortcut to global simulation clock
    protected EventList eventList;       // events to be processed are stored here

    /**
     * Service Points are created in simu.model-package's class inheriting the Engine class
     */
    public Engine() {
        clock = Clock.getInstance();
        eventList = new EventList();
    }

    /**
     * Define how long we will run the simulation.
     * @param time Ending time of the simulation
     */
    public void setSimulationTime(double time) {
        simulationTime = time;
    }

    /**
     * The starting point of the simulator. Returns when the simulation ends.
     */
    public void run() {
        initialize();

        while (simulate()) {
            double nextTime = currentTime();
            if (!Double.isNaN(nextTime) && nextTime > clock.getClock()) {
                Trace.out(Trace.Level.INFO, "\nA-phase: time is " + nextTime);
                clock.advanceTo(nextTime);
            }

            Trace.out(Trace.Level.INFO, "\nB-phase:");
            runBEvents();

            Trace.out(Trace.Level.INFO, "\nC-phase:");
            tryCEvents();
        }

        results();
    }


    /**
     * Execute all B-events (bound to time) at the current time, removing them from the event list.
     */
    private void runBEvents() {
        while (!eventList.isEmpty() && eventList.getNextEventTime() == clock.getClock()) {
            Event e = eventList.remove();
            runEvent(e);
        }
    }

    /**
     * @return Earliest event time in the event list
     */
    private double currentTime() {
        return eventList.isEmpty() ? Double.NaN : eventList.getNextEventTime();
    }

    /**
     * @return true if we should continue simulation
     */
    private boolean simulate() {
        return !eventList.isEmpty() && clock.getClock() < simulationTime;
    }

    /**
     * Execute event actions (e.g., removing visitor from the queue).
     * Defined in simu.model-package's class inheriting Engine.
     *
     * @param e The event to be executed
     */
    protected abstract void runEvent(Event e);

    /**
     * Execute all possible C-events (conditional events).
     * Defined in simu.model-package's class inheriting Engine.
     */
    protected abstract void tryCEvents();

    /**
     * Set all data structures to initial values.
     * Defined in simu.model-package's class inheriting Engine.
     */
    protected abstract void initialize();

    /**
     * Show/analyze measurement parameters collected during the simulation.
     * Called at the end of the simulation.
     */
    protected abstract void results();
}