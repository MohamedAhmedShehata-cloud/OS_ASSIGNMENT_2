package os.engine;

import os.scheduler.SchedulingStrategy;
import os.model.Process;
import os.observer.SimulationObserver;
import os.input.TestCase;
import java.util.List;

/**
 * ============================================================================
 * OWNER: Mohamed Mamdouh (Architect & ACS Engine)
 * RESPONSIBILITY: Central simulation loop driving discrete clock ticks and notifications
 * ============================================================================
 */
public class SimulationEngine {
    private SimulationClock clock;
    private SchedulingStrategy strategy;
    private ContextSwitchManager csManager;
    private List<SimulationObserver> observers;
    private List<Process> incomingProcesses;
    private Process currentlyRunning;

    /**
     * Constructs a simulation engine with a given scheduling strategy and input scenario.
     *
     * @param strategy scheduling strategy to execute (ACS, SJF)
     * @param testCase test case containing process specs and configuration
     */
    public SimulationEngine(SchedulingStrategy strategy, TestCase testCase) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Registers an observer to receive simulation lifecycle event callbacks.
     *
     * @param observer simulation observer (logger, metrics collector, UI)
     */
    public void addObserver(SimulationObserver observer) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Starts and executes the main simulation discrete-time event loop until completion.
     */
    public void run() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Checks if all processes have completed and no context switch or queue is pending.
     *
     * @return true if simulation is fully finished, false otherwise
     */
    public boolean isCompleted() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}