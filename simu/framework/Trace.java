package simu.framework;

/**
 * General output for diagnostic messages. Every diagnostic message has a severity level.
 * It is possible to control which level of diagnostic messages is printed.
 */
public class Trace {
    /**
     * Diagnostic message severity level
     */
    public enum Level {
        /** Informational messages */
        INFO,
        /** Warning messages */
        WARN,
        /** Error messages */
        ERR
    }

    // Default severity level filtering
    private static Level traceLevel = Level.INFO;

    /**
     * Set the filtering level of the diagnostic messages.
     * Messages below this level will not be printed.
     *
     * @param lvl filtering level
     */
    public static void setTraceLevel(Level lvl) {
        traceLevel = lvl;
    }

    /**
     * Print the given diagnostic message to the console.
     *
     * @param lvl severity level of the diagnostic message
     * @param txt diagnostic message to be printed
     */
    public static void out(Level lvl, String txt) {
        if (lvl.ordinal() >= traceLevel.ordinal()) {
            double currentTime = Clock.getInstance().getClock();
            System.out.println("[" + lvl + " @ " + currentTime + "] " + txt);
        }
    }
}