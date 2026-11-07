package os.observer;

import os.model.Process;
import os.metrics.SystemMetricsReport;
import java.util.List;

/**
 * ============================================================================
 * OWNER: Youssef Samir (Data Models & Metrics)
 * RESPONSIBILITY: Console logger observer formatting terminal output and demotion events
 * ============================================================================
 */
public class ConsoleLogger implements SimulationObserver {

    /**
     * Formats and logs current time step details including running process and queue states.
     */
    @Override
    public void onTick(int currentTime, Process runningProcess, List<Process> tier1Queue, List<Process> tier2Queue) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Formats and logs context switch transition messages.
     */
    @Override
    public void onContextSwitch(int currentTime, Process outgoing, Process incoming) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Formats and logs process completion notifications.
     */
    @Override
    public void onProcessFinished(int currentTime, Process p) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Triggers printing of the final summary metrics table upon simulation completion.
     */
    @Override
    public void onSimulationCompleted(SystemMetricsReport report) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}