package main.java.os.observer;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import main.java.os.model.Process;
import main.java.os.metrics.ProcessMetrics;
import main.java.os.metrics.SystemMetricsReport;

/**
 * ============================================================================
 * OWNER: Youssef Samir (Data Models & Metrics)
 * RESPONSIBILITY: Collects timestamps and computes WT, TAT, RT upon simulation finish
 * ============================================================================
 */
public class MetricsCollector implements SimulationObserver {

    private Map<String, ProcessMetrics> metricsMap;

    /**
     * Initializes a new MetricsCollector with an empty collection map.
     */
    public MetricsCollector() {
        metricsMap = new HashMap<>();
    }

    /**
     * Records or updates tick information if needed.
     */
    @Override
    public void onTick(int currentTime, Process running, List<Process> t1, List<Process> t2) {
        // No calculations are needed here.
        // The Process object already records its first execution time.
    }

    /**
     * Records context switch events for utilization and overhead analysis.
     */
    @Override
    public void onContextSwitch(int currentTime, Process outgoing, Process incoming) {
        // No context switch metrics are currently being collected.
    }

    /**
     * Calculates Turnaround Time, Waiting Time, and Response Time upon process completion and stores metrics.
     */
    @Override
    public void onProcessFinished(int currentTime, Process p) {

        int turnaroundTime = currentTime - p.getArrivalTime();

        int waitingTime = turnaroundTime - p.getBurstTime();

        int responseTime = p.getFirstExecutionTime() - p.getArrivalTime();

        ProcessMetrics metrics = new ProcessMetrics(
            p.getId(),
            p.getArrivalTime(),
            currentTime,
            turnaroundTime,
            waitingTime,
            responseTime
        );

        metricsMap.put(p.getId(), metrics);
    }

    /**
     * Handles simulation completion event.
     */
    @Override
    public void onSimulationCompleted(SystemMetricsReport report) {
        // The final report can be generated using generateReport().
    }

    /**
     * Aggregates all recorded process metrics into a finalized SystemMetricsReport.
     *
     * @return aggregated system metrics report
     */
    public SystemMetricsReport generateReport() {

        List<ProcessMetrics> metricsList =
            new java.util.ArrayList<>(metricsMap.values());

        return new SystemMetricsReport(metricsList);
    }
}