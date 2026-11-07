package main.java.os.gui.view;

import java.util.List;
import main.java.os.model.Process;

/**
 * ============================================================================
 * OWNER: Omar Osama (GUI & Visualization Specialist)
 * RESPONSIBILITY: Displays queued processes in Tier 1 and Tier 2 with real-time updates
 * ============================================================================
 */
public class QueueStatePanel {

    /**
     * Refreshes the visual list of processes waiting in Tier 1.
     *
     * @param processes snapshot of processes in Tier 1
     */
    public void updateTier1(List<Process> processes) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Refreshes the visual list of processes waiting in Tier 2 with their effective priorities.
     *
     * @param processes snapshot of processes in Tier 2
     */
    public void updateTier2(List<Process> processes) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}