package main.java.os.engine;

import java.util.ArrayList;
import java.util.List;
import main.java.os.model.Process;
import main.java.os.scheduler.SchedulingStrategy;
import main.java.os.observer.SimulationObserver;
import main.java.os.input.TestCase;

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
        clock = new SimulationClock();

        this.strategy = strategy;
        csManager = new ContextSwitchManager(testCase.contextSwitchTime());
        observers = new ArrayList<>();
        incomingProcesses = testCase.processes();
    }

    /**
     * Registers an observer to receive simulation lifecycle event callbacks.
     *
     * @param observer simulation observer (logger, metrics collector, UI)
     */
    public void addObserver(SimulationObserver observer) {
        observers.add(observer);
    }

    /**
     * Starts and executes the main simulation discrete-time event loop until completion.
     */
    public void run() {
        while (!isCompleted()) {
            int currentTime = clock.getCurrentTime();

            for (Process p : incomingProcesses) {
                if (p.getArrivalTime() == currentTime) {
                    strategy.addProcess(p, currentTime);
                }
            }

            if (csManager.isSwitching()) {
                csManager.tick();
                if (!csManager.isSwitching()) {
                    currentlyRunning = csManager.getIncomingProcess();
                }
            } else {
                if (currentlyRunning == null || strategy.shouldPreempt(currentlyRunning, currentTime)) {
                    Process next = strategy.selectNextProcess(currentTime);
                    if (csManager.isSwitchRequired(currentlyRunning, next)) {
                        csManager.startSwitch(currentlyRunning, next);
                        currentlyRunning = null;
                    } else {
                        currentlyRunning = next;
                    }
                }

                if (currentlyRunning != null) {
                    currentlyRunning.executeOneUnit(currentTime);
                    strategy.onTick(currentTime, currentlyRunning);
                    if (currentlyRunning.isCompleted()) {
                        currentlyRunning.setFinishTime(currentTime + 1);
                        currentlyRunning = null;
                    }
                }
            }

            for (SimulationObserver observer : observers) {
                observer.onTick(
                        currentTime, currentlyRunning,
                        strategy.getTier1Queue(), strategy.getTier2Queue()
                );
            }

            clock.tick();
        }
    }

    /**
     - Checks if all processes have completed and no context switch or queue is pending.
     - @return true if simulation is fully finished, false otherwise
     */
    public boolean isCompleted() {
        for (Process p : incomingProcesses) {
            if (!p.isCompleted())
                return false;
        }
        return true;
    }
}