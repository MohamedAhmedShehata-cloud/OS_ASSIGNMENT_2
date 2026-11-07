package os.scheduler.acs;

import os.model.Process;
import java.util.List;
import java.util.PriorityQueue;

/**
 * ============================================================================
 * OWNER: Mohamed Mamdouh (Architect & ACS Engine)
 * RESPONSIBILITY: Non-Preemptive Priority queue ordered by effective priority
 * ============================================================================
 */
public class Tier2Queue {
    private PriorityQueue<Process> queue;

    /**
     * Constructs a Tier 2 priority queue ordered by effective priority then arrival time.
     */
    public Tier2Queue() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Enqueues a demoted process into Tier 2 and records its Tier 2 entry timestamp.
     *
     * @param p demoted process
     * @param currentTime timestamp of entry into Tier 2
     */
    public void enqueue(Process p, int currentTime) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Dequeues the highest priority process from Tier 2.
     *
     * @return highest priority process, or null if empty
     */
    public Process dequeue() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Peeks at the highest priority process without removing it.
     *
     * @return highest priority process, or null if empty
     */
    public Process peek() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Checks if Tier 2 queue is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns a snapshot list of processes currently in Tier 2.
     *
     * @return list of processes
     */
    public List<Process> asList() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Recalculates effective priorities of all waiting processes in Tier 2 using AgingPolicy and re-sorts.
     *
     * @param currentTime current clock tick
     */
    public void refreshPriorities(int currentTime) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}