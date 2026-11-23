package main.java.os.observer;

import java.util.List;
import main.java.os.model.Process;
import main.java.os.metrics.SystemMetricsReport;

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
        System.out.println("Time:"+currentTime);
        if(runningProcess==null)
        {
            System.out.println("CPU: Idle or Context Switching");
        }else {System.out.println("Running:"+runningProcess.getId());}
         System.out.println("Tier 1:"+tier1Queue);
         System.out.println("Tier 2:"+tier2Queue);
    }

    /**
     * Formats and logs context switch transition messages.
     */
    @Override
    public void onContextSwitch(int currentTime, Process outgoing, Process incoming) {
       System.out.println("Time:"+currentTime);
       
    String from = (outgoing == null) ? "Idle" : outgoing.getId();
    String to = (incoming == null) ? "Idle" : incoming.getId();

    System.out.println("Context Switch: " + from + " --> " + to);
    }

    /**
     * Formats and logs process completion notifications.
     */
    @Override
    public void onProcessFinished(int currentTime, Process p) {
        System.out.println( "Time :" + currentTime + " Process: " + p.getId() + " completed");       
    }

    /**
     * Triggers printing of the final summary metrics table upon simulation completion.
     */
    @Override
    public void onSimulationCompleted(SystemMetricsReport report) {
            System.out.println("\n===== SIMULATION COMPLETED =====");
                       report.printSummaryTable();
    }
}