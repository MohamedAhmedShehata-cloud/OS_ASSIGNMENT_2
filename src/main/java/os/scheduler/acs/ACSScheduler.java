package os.scheduler.acs;

import os.scheduler.SchedulingStrategy;
import os.model.Process;
import java.util.List;

/**
 * ============================================================================
 * OWNER: Mohamed Mamdouh (Architect & ACS Engine)
 * RESPONSIBILITY: Coordinates Tier 1 and Tier 2 transitions, demotions, and aging
 * ============================================================================
 */
public class ACSScheduler implements SchedulingStrategy {
    private Tier1Queue tier1;
    private Tier2Queue tier2;
    private boolean isProcessRunningNonPreemptively;

    /**
     * Constructs an Adaptive Container Scheduler instance with specified Tier 1 quantum.
     *
     * @param timeQuantum round robin quantum for Tier 1
     */
    public ACSScheduler(int timeQuantum) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Enters a newly arrived process into Tier 1 queue.
     */
    @Override
    public void addProcess(Process p, int currentTime) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Selects next process: checks Tier 1 first; if Tier 1 is empty, refreshes priorities in Tier 2 and selects head.
     */
    @Override
    public Process selectNextProcess(int currentTime) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Handles quantum progress in Tier 1 and executes demotion to Tier 2 if quantum expires without completion.
     */
    @Override
    public void onTick(int currentTime, Process running) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Determines preemption: processes in Tier 2 run non-preemptively; Tier 1 preempts when quantum expires.
     */
    @Override
    public boolean shouldPreempt(Process currentlyRunning, int currentTime) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Resets non-preemptive lock flag when a Tier 2 process finishes execution.
     */
    public void markNonPreemptiveCompleted() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns snapshot of Tier 1 queue.
     */
    @Override
    public List<Process> getTier1Queue() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns snapshot of Tier 2 queue.
     */
    @Override
    public List<Process> getTier2Queue() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns scheduler name string.
     */
    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}