package test;

import simu.framework.Engine;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.MyEngine;

/**
 * Command-line type User Interface
 *
 * With setTraceLevel() you can control the number of diagnostic messages printed to the console.
 */
public class Simulator {
    public static void main(String[] args) {
        // Control diagnostic output level
        Trace.setTraceLevel(Level.INFO);

        // Example parameters (these could later come from UI or config file)
        double arrivalMean = 10.0;
        double ticketMean = 5.0, ticketStd = 2.0;
        double restMean = 2.0, restStd = 1.0;
        double attr1Mean = 10.0, attr1Std = 3.0;
        double attr2Mean = 8.0, attr2Std = 2.0;
        double attr3Mean = 12.0, attr3Std = 4.0;

        // Create engine with configurable distributions
        Engine m = new MyEngine(arrivalMean,
                ticketMean, ticketStd,
                restMean, restStd,
                attr1Mean, attr1Std,
                attr2Mean, attr2Std,
                attr3Mean, attr3Std);

        // Define simulation length
        m.setSimulationTime(1000);

        // Run simulation
        m.run();
    }
}
