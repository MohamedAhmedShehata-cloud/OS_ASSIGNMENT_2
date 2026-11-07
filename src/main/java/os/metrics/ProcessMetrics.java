package os.metrics;

/**
 * ============================================================================
 * OWNER: Youssef Samir (Data Models & Metrics)
 * RESPONSIBILITY: Holds final calculated metrics for a single completed process
 * ============================================================================
 *
 * @param processId identifier of the process
 * @param arrivalTime timestamp when process arrived
 * @param finishTime timestamp when process finished execution
 * @param turnaroundTime total elapsed time from arrival to completion (TAT = finish - arrival)
 * @param waitingTime total time spent waiting in ready queues (WT = TAT - burst)
 * @param responseTime time elapsed from arrival until first execution on CPU (RT = firstExec - arrival)
 */
public record ProcessMetrics(
    String processId,
    int arrivalTime,
    int finishTime,
    int turnaroundTime,
    int waitingTime,
    int responseTime
) {}