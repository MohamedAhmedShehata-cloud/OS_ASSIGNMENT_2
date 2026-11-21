package main.java.os.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import main.java.os.model.Process;
import main.java.os.model.ProcessState;
import main.java.os.model.SchedulerTier;

/**
 * ============================================================================
 * OWNER: Mohamed Ahmed (SJF & Context Switch Engine)
 * RESPONSIBILITY: Preemptive Shortest-Job First (SRTF) algorithm implementation
 * ============================================================================
 */
public class SJFScheduler implements SchedulingStrategy {
    private final PriorityQueue<Process> readyQueue;
    private final Comparator<Process> comparator;

    /**
     * Constructs a preemptive Shortest-Job First (SRTF) scheduler.
     */
    public SJFScheduler() {
        this.comparator = Comparator.comparingInt(Process::getRemainingTime)
                .thenComparingInt(Process::getArrivalTime)
                .thenComparing(Process::getId);
        this.readyQueue = new PriorityQueue<>(comparator);
    }

    /**
     * Adds an arriving process to the SJF ready queue ordered by remaining burst time.
     */
    @Override
    public void addProcess(Process p, int currentTime) {
        if (p != null) {
            p.setState(ProcessState.READY);
            p.setCurrentTier(SchedulerTier.NONE);
            readyQueue.offer(p);
        }
    }

    /**
     * Selects and polls the process with the shortest remaining burst time.
     */
    @Override
    public Process selectNextProcess(int currentTime) {
        return readyQueue.poll();
    }

    /**
     * Tick callback for currently executing process.
     */
    @Override
    public void onTick(int currentTime, Process currentlyRunning) {
        // Preemptive SJF does not require quantum slice tracking.
    }

    /**
     * Preempts currently running process if an arriving process has strictly shorter remaining burst time.
     */
    @Override
    public boolean shouldPreempt(Process currentlyRunning, int currentTime) {
        if (currentlyRunning == null || currentlyRunning.isCompleted()) {
            return false;
        }
        if (readyQueue.isEmpty()) {
            return false;
        }
        Process candidate = readyQueue.peek();
        return candidate.getRemainingTime() < currentlyRunning.getRemainingTime();
    }

    /**
     * Returns snapshot of ready queue (represented in Tier 1 slot for reporting).
     */
    @Override
    public List<Process> getTier1Queue() {
        List<Process> snapshot = new ArrayList<>(readyQueue);
        snapshot.sort(comparator);
        return Collections.unmodifiableList(snapshot);
    }

    /**
     * Returns empty list since SJF does not utilize a secondary tier.
     */
    @Override
    public List<Process> getTier2Queue() {
        return Collections.emptyList();
    }

    /**
     * Returns algorithm name string.
     */
    @Override
    public String getName() {
        return "Preemptive Shortest-Job First (SJF/SRTF)";
    }
}
