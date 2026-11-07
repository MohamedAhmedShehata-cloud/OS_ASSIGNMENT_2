package main.java.os.scheduler;

import java.util.List;
import main.java.os.model.Process;

/**
 * ============================================================================
 * OWNER: Mohamed Mamdouh (Architect & ACS Engine)
 * RESPONSIBILITY: Unified scheduling strategy interface (Strategy Pattern)
 * ============================================================================
 */
public interface SchedulingStrategy {
    /**
     * Adds a newly arrived process to the scheduling strategy at currentTime.
     *
     * @param p the arriving process
     * @param currentTime arrival timestamp
     */
    void addProcess(Process p, int currentTime);

    /**
     * Selects and dequeues the next candidate process to be scheduled on the CPU.
     *
     * @param currentTime current clock tick
     * @return process to dispatch, or null if no process is eligible
     */
    Process selectNextProcess(int currentTime);

    /**
     * Handles clock tick progression for the currently running process (e.g. quantum tracking).
     *
     * @param currentTime current clock tick
     * @param currentlyRunning process currently executing on CPU
     */
    void onTick(int currentTime, Process currentlyRunning);

    /**
     * Evaluates whether the currently running process should be preempted.
     *
     * @param currentlyRunning process currently running on CPU
     * @param currentTime current clock tick
     * @return true if preemption must occur, false otherwise
     */
    boolean shouldPreempt(Process currentlyRunning, int currentTime);

    /**
     * Retrieves an immutable or snapshot view of the Tier 1 ready queue.
     *
     * @return list of processes in Tier 1
     */
    List<Process> getTier1Queue();

    /**
     * Retrieves an immutable or snapshot view of the Tier 2 ready queue.
     *
     * @return list of processes in Tier 2
     */
    List<Process> getTier2Queue();

    /**
     * Returns the human-readable display name of the scheduling algorithm.
     *
     * @return algorithm name string
     */
    String getName();
}