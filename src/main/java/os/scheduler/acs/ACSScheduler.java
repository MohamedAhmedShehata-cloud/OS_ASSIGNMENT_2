package main.java.os.scheduler.acs;

import java.util.List;

import main.java.os.model.ProcessState;
import main.java.os.model.SchedulerTier;
import main.java.os.scheduler.SchedulingStrategy;
import main.java.os.model.Process;

/**
 - ============================================================================
 - OWNER: Mohamed Mamdouh (Architect & ACS Engine)
 - RESPONSIBILITY: Coordinates Tier 1 and Tier 2 transitions, demotions, and aging
 - ============================================================================
 */
public class ACSScheduler implements SchedulingStrategy {
    private final String name;
    private Tier1Queue tier1;
    private Tier2Queue tier2;
    private boolean isProcessRunningNonPreemptively;
    private boolean preemptionRequired;

    /**
     - Constructs an Adaptive Container Scheduler instance with specified Tier 1 quantum.
     *
     - @param timeQuantum round robin quantum for Tier 1
     */
    public ACSScheduler(String name, int timeQuantum) {
        this.name = name;

        if (timeQuantum <= 0) {
            throw new IllegalArgumentException();
        }
        tier1 = new Tier1Queue(timeQuantum);
        tier2 = new Tier2Queue();
        isProcessRunningNonPreemptively = false;
        preemptionRequired = false;
    }

    /**
     - Enters a newly arrived process into Tier 1 queue.
     */
    @Override
    public void addProcess(Process p, int currentTime) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        p.setCurrentTier(SchedulerTier.TIER_1);
        p.setState(ProcessState.READY);
        tier1.enqueue(p);
    }

    /**
     - Selects next process: checks Tier 1 first; if Tier 1 is empty, refreshes priorities in Tier 2 and selects head.
     */
    @Override
    public Process selectNextProcess(int currentTime) {
        if (!tier1.isEmpty()) {
            Process next = tier1.dequeue();
            if (next != null) {
                next.setCurrentTier(SchedulerTier.TIER_1);
                tier1.resetQuantumUsage();
                isProcessRunningNonPreemptively = false;
                preemptionRequired = false;
            }
            return next;
        }
        if (!tier2.isEmpty()) {
            tier2.refreshPriorities(currentTime);
            Process next = tier2.dequeue();
            if (next != null) {
                next.setCurrentTier(SchedulerTier.TIER_2);
                isProcessRunningNonPreemptively = true;
                preemptionRequired = false;
            }
            return next;
        }
        return null;
    }

    /**
     - Handles quantum progress in Tier 1 and executes demotion to Tier 2 if quantum expires without completion.
     */
    @Override
    public void onTick(int currentTime, Process running) {
        if (running == null || running.isCompleted()) {
            return;
        }
        if (running.getCurrentTier() == SchedulerTier.TIER_1) {
            tier1.incrementQuantumUsage();
            if (tier1.hasExpiredQuantum()) {
                running.setCurrentTier(SchedulerTier.TIER_2);
                running.setState(ProcessState.READY);
                tier2.enqueue(running, currentTime);
                tier1.resetQuantumUsage();
                preemptionRequired = true;
            }
        }
    }

    /**
     - Determines preemption: processes in Tier 2 run non-preemptively; Tier 1 preempts when quantum expires.
     */
    @Override
    public boolean shouldPreempt(Process currentlyRunning, int currentTime) {
        if (currentlyRunning == null || currentlyRunning.isCompleted()) {
            return false;
        }
        if (preemptionRequired) {
            preemptionRequired = false;
            return true;
        }
        if (isProcessRunningNonPreemptively || currentlyRunning.getCurrentTier() == SchedulerTier.TIER_2) {
            return false;
        }
        return tier1.hasExpiredQuantum();
    }

    /**
     - Resets non-preemptive lock flag when a Tier 2 process finishes execution.
     */
    public void markNonPreemptiveCompleted() {
        isProcessRunningNonPreemptively = false;
    }

    /**
     - Returns snapshot of Tier 1 queue.
     */
    @Override
    public List<Process> getTier1Queue() {
        return tier1.asList();
    }

    /**
     - Returns snapshot of Tier 2 queue.
     */
    @Override
    public List<Process> getTier2Queue() {
        return tier2.asList();
    }

    /**
     - Returns scheduler name string.
     */
    @Override
    public String getName() {
        return name;
    }
}