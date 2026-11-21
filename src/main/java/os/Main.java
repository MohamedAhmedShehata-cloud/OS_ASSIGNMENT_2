package main.java.os;

import main.java.os.engine.SimulationEngine;
import main.java.os.input.TestCase;
import main.java.os.input.TestCaseFactory;
import main.java.os.observer.ConsoleLogger;
import main.java.os.observer.MetricsCollector;
import main.java.os.scheduler.SJFScheduler;
import main.java.os.scheduler.SchedulingStrategy;
import main.java.os.scheduler.acs.ACSScheduler;

/**
 * ============================================================================
 * OWNER: Mohamed Ahmed (SJF & Context Switch Engine)
 * RESPONSIBILITY: Main application entry point for running test cases and benchmarks
 * ============================================================================
 */
public class Main {
    /**
     * Application entry point: initializes schedulers, feeds test cases, and runs simulations.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("       Cloud-Cluster CPU Scheduling Simulator - CS241 (Fall 2026)");
        System.out.println("================================================================================\n");

        // Execute Test Case 1 with both Preemptive SJF and ACS
        TestCase tc1 = TestCaseFactory.getTestCase1();
        runSimulation(new SJFScheduler(), tc1);
        runSimulation(new ACSScheduler(tc1.timeQuantum()), tc1);

        // Execute Test Case 2 with both Preemptive SJF and ACS
        TestCase tc2 = TestCaseFactory.getTestCase2();
        runSimulation(new SJFScheduler(), tc2);
        runSimulation(new ACSScheduler(tc2.timeQuantum()), tc2);
    }

    /**
     * Helper method to initialize engine, register observers, and execute the scenario.
     */
    private static void runSimulation(SchedulingStrategy strategy, TestCase testCase) {
        System.out.println("================================================================================");
        System.out.println(">>> Algorithm: " + strategy.getName());
        System.out.println(">>> Scenario : " + testCase.name() + " (Quantum=" + testCase.timeQuantum() 
                + ", CS=" + testCase.contextSwitchTime() + ")");
        System.out.println("================================================================================");

        SimulationEngine engine = new SimulationEngine(strategy, testCase);
        engine.addObserver(new ConsoleLogger());
        MetricsCollector collector = new MetricsCollector();
        engine.addObserver(collector);

        engine.run();
        System.out.println("\n");
    }
}
