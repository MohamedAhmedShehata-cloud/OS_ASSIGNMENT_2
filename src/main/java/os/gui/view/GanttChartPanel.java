package os.gui.view;

import os.model.Process;

/**
 * ============================================================================
 * OWNER: Omar Osama (GUI & Visualization Specialist)
 * RESPONSIBILITY: Visualizes live Gantt chart execution blocks and context switches
 * ============================================================================
 */
public class GanttChartPanel {

    /**
     * Appends a time slot to the Gantt chart display representing execution or context switch.
     *
     * @param time clock tick index
     * @param process executing process, or null during idle/switch
     * @param isContextSwitch true if the slot represents a context switch overhead
     */
    public void appendSlot(int time, Process process, boolean isContextSwitch) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Clears all drawn slots from the Gantt chart canvas.
     */
    public void clear() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}