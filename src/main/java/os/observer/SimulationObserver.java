package main.java.os.observer;

import java.util.List;
import main.java.os.model.Process;
import main.java.os.metrics.SystemMetricsReport;

/**
 * ============================================================================
 * OWNER: Youssef Samir (Data Models & Metrics)
 * RESPONSIBILITY: Observer interface for decoupling simulation events from listeners
 * ============================================================================
 */
public interface SimulationObserver {
    /**
     * Notifies the observer of a clock tick event and the current states of queues and CPU.
     *
     * @param currentTime the current simulation timestamp
     * @param runningProcess the process currently occupying the CPU, or null if idle/switching
     * @param tier1Queue current snapshot of processes in Tier 1 queue
     * @param tier2Queue current snapshot of processes in Tier 2 queue
     */
    void onTick(int currentTime, Process runningProcess, List<Process> tier1Queue, List<Process> tier2Queue);

    /**
     * Notifies the observer that a CPU context switch has begun between two processes.
     *
     * @param currentTime the timestamp at which the context switch started
     * @param outgoingProcess the process being preempted or evicted, or null
     * @param incomingProcess the process to be loaded next onto the CPU
     */
    void onContextSwitch(int currentTime, Process outgoingProcess, Process incomingProcess);

    /**
     * Notifies the observer that a process has completed execution.
     *
     * @param currentTime completion timestamp
     * @param completedProcess the process that finished execution
     */
    void onProcessFinished(int currentTime, Process completedProcess);

    /**
     * Notifies the observer that the simulation has completely finished and provides the final metrics.
     *
     * @param report aggregated system metrics report
     */
    void onSimulationCompleted(SystemMetricsReport report);
}