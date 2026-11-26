package main.java.os.scheduler.acs;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import main.java.os.model.Process;

/**
 * ============================================================================
 * OWNER: Mohamed Mamdouh (Architect & ACS Engine)
 * RESPONSIBILITY: Round Robin queue tracking time quantum consumption
 * ============================================================================
 */
public class Tier1Queue {
    private Queue<Process> queue;
    private int timeQuantum;
    private int currentQuantumUsed;

    /**
     * Constructs a Tier 1 Round Robin queue with a specified time quantum.
     *
     * @param timeQuantum maximum consecutive ticks a process may execute in Tier 1
     */
    public Tier1Queue(int timeQuantum) {
        if (timeQuantum <= 0)
            throw new IllegalArgumentException();

        queue = new LinkedList<>();
        this.timeQuantum = timeQuantum;
        currentQuantumUsed = 0;
    }

    /**
     * Inserts a process at the tail of the Tier 1 queue.
     *
     * @param p process to enqueue
     */
    public void enqueue(Process p) {
        queue.add(p);
    }

    /**
     * Removes and returns the process at the head of the queue.
     *
     * @return head process, or null if empty
     */
    public Process dequeue() {
        return queue.isEmpty() ? null : queue.remove();
    }

    /**
     * Inspects the process at the head of the queue without removing it.
     *
     * @return head process, or null if empty
     */
    public Process peek() {
        return queue.isEmpty() ? null : queue.peek();
    }

    /**
     * Checks whether the Tier 1 queue contains any processes.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Returns a snapshot list of all processes currently queued in Tier 1.
     *
     * @return list of queued processes
     */
    public List<Process> asList() {
        return queue.stream().toList();
    }

    /**
     * Increments the quantum usage counter for the currently executing process.
     */
    public void incrementQuantumUsage() {
        currentQuantumUsed++;
    }

    /**
     * Resets the quantum usage counter back to zero.
     */
    public void resetQuantumUsage() {
        currentQuantumUsed = 0;
    }

    /**
     * Checks if the currently dispatched process has exhausted its allocated quantum.
     *
     * @return true if quantum usage >= timeQuantum, false otherwise
     */
    public boolean hasExpiredQuantum() {
        return currentQuantumUsed >= timeQuantum;
    }

    /**
     * Retrieves the number of ticks consumed in the current quantum slice.
     *
     * @return quantum ticks used so far
     */
    public int getCurrentQuantumUsed() {
        return currentQuantumUsed;
    }
}