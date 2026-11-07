package os.scheduler;

import os.model.Process;
import java.util.List;
import java.util.PriorityQueue;

/**
 * ============================================================================
 * OWNER: Mohamed Ahmed (SJF & Context Switch Engine)
 * RESPONSIBILITY: Preemptive Shortest-Job First (SRTF) algorithm implementation
 * ============================================================================
 */
public class SJFScheduler implements SchedulingStrategy {
    private PriorityQueue<Process> readyQueue;

    /**
     * Constructs a preemptive Shortest-Job First (SRTF) scheduler.
     */
    public SJFScheduler() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Adds an arriving process to the SJF ready queue ordered by remaining burst time.
     */
    @Override
    public void addProcess(Process p, int currentTime) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Selects and polls the process with the shortest remaining burst time.
     */
    @Override
    public Process selectNextProcess(int currentTime) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Tick callback for currently executing process.
     */
    @Override
    public void onTick(int currentTime, Process currentlyRunning) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Preempts currently running process if an arriving process has strictly shorter remaining burst time.
     */
    @Override
    public boolean shouldPreempt(Process currentlyRunning, int currentTime) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns snapshot of ready queue (represented in Tier 1 slot for reporting).
     */
    @Override
    public List<Process> getTier1Queue() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns empty list since SJF does not utilize a secondary tier.
     */
    @Override
    public List<Process> getTier2Queue() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns algorithm name string.
     */
    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}