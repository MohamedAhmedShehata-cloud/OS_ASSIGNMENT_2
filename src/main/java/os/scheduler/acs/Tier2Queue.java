package main.java.os.scheduler.acs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import main.java.os.model.Process;

/**
 * ============================================================================
 * OWNER: Mohamed Mamdouh (Architect & ACS Engine)
 * RESPONSIBILITY: Non-Preemptive Priority queue ordered by effective priority
 * ============================================================================
 */
public class Tier2Queue {
    private PriorityQueue<Process> queue;
    private static final Comparator<Process> ORDER = Comparator
            .comparingInt(Process::getEffectivePriority)
            .thenComparingInt(Process::getArrivalTime);
    /**
     * Constructs a Tier 2 priority queue ordered by effective priority then arrival time.
     */
    public Tier2Queue() {
        queue = new PriorityQueue<>(ORDER);
    }

    /**
     * Enqueues a demoted process into Tier 2 and records its Tier 2 entry timestamp.
     *
     * @param p demoted process
     * @param currentTime timestamp of entry into Tier 2
     */
    public void enqueue(Process p, int currentTime) {
        p.setTier2ArrivalTime(currentTime);
        queue.add(p);
    }

    /**
     * Dequeues the highest priority process from Tier 2.
     *
     * @return highest priority process, or null if empty
     */
    public Process dequeue() {
        return queue.isEmpty() ? null : queue.remove();
    }

    /**
     * Peeks at the highest priority process without removing it.
     *
     * @return highest priority process, or null if empty
     */
    public Process peek() {
        return queue.peek();
    }

    /**
     * Checks if Tier 2 queue is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Returns a snapshot list of processes currently in Tier 2.
     *
     * @return list of processes
     */
    public List<Process> asList() {
        List<Process> list = new ArrayList<>(queue);
        list.sort(ORDER);

        return list;
    }

    /**
     * Recalculates effective priorities of all waiting processes in Tier 2 using AgingPolicy and re-sorts.
     *
     * @param currentTime current clock tick
     */
    public void refreshPriorities(int currentTime) {
        List<Process> processes = asList();

        queue.clear();
        for (Process p : processes) {
            AgingPolicy.applyAging(p, currentTime);
            queue.add(p);
        }
    }
}