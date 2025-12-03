package simu.framework;

/**
 * Singleton for holding global simulation time.
 * Provides methods to get, set, advance, and reset the clock.
 */
public class Clock {
    private double clock;
    private static Clock instance;

    private Clock() {
        clock = 0;
    }

    public static Clock getInstance() {
        if (instance == null) {
            instance = new Clock();
        }
        return instance;
    }

    /**
     * Set the clock to a specific time.
     */
    public void setClock(double clock) {
        this.clock = clock;
    }

    /**
     * Advance the clock forward to a new time.
     * @param newTime the new simulation time
     */
    public void advanceTo(double newTime) {
        if (newTime >= clock) {
            clock = newTime;
        } else {
            Trace.out(Trace.Level.WARN, "Attempted to move clock backwards to " + newTime);
        }
    }

    /**
     * Get the current simulation time.
     */
    public double getClock() {
        return clock;
    }

    /**
     * Reset the clock back to zero.
     */
    public void reset() {
        clock = 0;
    }
}