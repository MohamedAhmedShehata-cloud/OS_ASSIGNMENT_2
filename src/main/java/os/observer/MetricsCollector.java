package os.observer;

import os.model.Process;
import os.metrics.ProcessMetrics;
import os.metrics.SystemMetricsReport;
import java.util.List;
import java.util.Map;

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
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Records or updates tick information if needed.
     */
    @Override
    public void onTick(int currentTime, Process running, List<Process> t1, List<Process> t2) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Records context switch events for utilization and overhead analysis.
     */
    @Override
    public void onContextSwitch(int currentTime, Process outgoing, Process incoming) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Calculates Turnaround Time, Waiting Time, and Response Time upon process completion and stores metrics.
     */
    @Override
    public void onProcessFinished(int currentTime, Process p) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Handles simulation completion event.
     */
    @Override
    public void onSimulationCompleted(SystemMetricsReport report) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Aggregates all recorded process metrics into a finalized SystemMetricsReport.
     *
     * @return aggregated system metrics report
     */
    public SystemMetricsReport generateReport() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}