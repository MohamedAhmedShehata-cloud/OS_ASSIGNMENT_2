package main.java.os.metrics;

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

    if (metricsList == null) {
        throw new IllegalArgumentException("Metrics list cannot be null");
    }

    this.metricsList = List.copyOf(metricsList);

    if (this.metricsList.isEmpty()) {
        this.averageTurnaroundTime = 0;
        this.averageWaitingTime = 0;
        return;
    }

    int totaltat = 0;
    int totalwat = 0;

    for (ProcessMetrics metric : this.metricsList) {
        totaltat += metric.turnaroundTime();
        totalwat += metric.waitingTime();
    }

    this.averageTurnaroundTime =
            (double) totaltat / this.metricsList.size();

    this.averageWaitingTime =
            (double) totalwat / this.metricsList.size();
}

    /**
     * Prints the structured summary metrics table for all processes along with average TAT and WT.
     */
    public void printSummaryTable() {
        System.out.println("process id || arrivalTime || finishTime||turnaroundTime|| waitingTime|| responseTime ");


    for (ProcessMetrics metric : metricsList) {

        System.out.println(
            metric.processId() + " | " +
            metric.arrivalTime() + " | " +
            metric.finishTime() + " | " +
            metric.turnaroundTime() + " | " +
            metric.waitingTime() + " | " +
            metric.responseTime()
        );
    }
      System.out.println("Average Tat: " + averageTurnaroundTime);
      System.out.println("Average Wt: " + averageWaitingTime);
    }

    /**
     * Retrieves the overall average turnaround time.
     *
     * @return average turnaround time
     */
    public double getAverageTurnaroundTime() {
       return averageTurnaroundTime;
    }

    /**
     * Retrieves the overall average waiting time.
     *
     * @return average waiting time
     */
    public double getAverageWaitingTime() {
       return averageWaitingTime;
    }

    /**
     * Retrieves the list of individual process metrics.
     *
     * @return list of ProcessMetrics
     */
    public List<ProcessMetrics> getMetricsList() {
     return metricsList;
    } 
}