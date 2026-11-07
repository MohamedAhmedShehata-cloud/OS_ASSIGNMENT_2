package main.java.os.model;

/**
 * ============================================================================
 * OWNER: Youssef Samir (Data Models & Metrics)
 * RESPONSIBILITY: Represents the lifecycle states of a process in the simulation
 * ============================================================================
 */
public enum ProcessState {
    /** Process has arrived and is waiting in one of the scheduler queues. */
    READY,

    /** Process is actively executing on the CPU. */
    RUNNING,

    /** Process is paused while CPU context switch overhead takes place. */
    CONTEXT_SWITCHING,

    /** Process has finished execution of its total burst time. */
    TERMINATED
}