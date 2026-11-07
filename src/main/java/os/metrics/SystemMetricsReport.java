package os.metrics;

import java.util.List;

/**
 * ============================================================================
 * OWNER: Youssef Samir (Data Models & Metrics)
 * RESPONSIBILITY: Aggregates system metrics (Average WT, Average TAT) and report generation
 * ============================================================================
 */
public class SystemMetricsReport {
    private List<ProcessMetrics> metricsList;
    private double averageTurnaroundTime;
    private double averageWaitingTime;

    /**
     * Constructs a system metrics report from a list of individual process metrics and computes averages.
     *
     * @param metricsList collection of completed process metrics
     */
    public SystemMetricsReport(List<ProcessMetrics> metricsList) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Prints the structured summary metrics table for all processes along with average TAT and WT.
     */
    public void printSummaryTable() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Retrieves the overall average turnaround time.
     *
     * @return average turnaround time
     */
    public double getAverageTurnaroundTime() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Retrieves the overall average waiting time.
     *
     * @return average waiting time
     */
    public double getAverageWaitingTime() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Retrieves the list of individual process metrics.
     *
     * @return list of ProcessMetrics
     */
    public List<ProcessMetrics> getMetricsList() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}