package main.java.os.engine;

import main.java.os.model.Process;

/**
 * ============================================================================
 * OWNER: Mohamed Ahmed (SJF & Context Switch Engine)
 * RESPONSIBILITY: Manages CPU occupancy during context switch duration (CS Time)
 * ============================================================================
 */
public class ContextSwitchManager {
    private int switchDuration;
    private int remainingSwitchTicks;
    private Process outgoingProcess;
    private Process incomingProcess;

    /**
     * Constructs a ContextSwitchManager with specified CS duration.
     *
     * @param switchDuration overhead ticks required to switch CPU context
     */
    public ContextSwitchManager(int switchDuration) {
        this.switchDuration = Math.max(0, switchDuration);
        this.remainingSwitchTicks = 0;
        this.outgoingProcess = null;
        this.incomingProcess = null;
    }

    /**
     * Evaluates whether a context switch overhead is necessary between outgoing and incoming processes.
     *
     * @param outgoing process leaving CPU
     * @param incoming process entering CPU
     * @return true if context switch is required, false otherwise
     */
    public boolean isSwitchRequired(Process outgoing, Process incoming) {
        if (switchDuration <= 0) {
            return false;
        }
        if (outgoing == null || incoming == null) {
            return false;
        }
        return !outgoing.getId().equals(incoming.getId());
    }

    /**
     * Initiates a context switch overhead period between outgoing and incoming processes.
     *
     * @param outgoing process being switched out
     * @param incoming process to be loaded next
     */
    public void startSwitch(Process outgoing, Process incoming) {
        this.outgoingProcess = outgoing;
        this.incomingProcess = incoming;
        this.remainingSwitchTicks = this.switchDuration;
    }

    /**
     * Decrements remaining context switch overhead ticks by one.
     */
    public void tick() {
        if (remainingSwitchTicks > 0) {
            remainingSwitchTicks--;
        }
    }

    /**
     * Checks if CPU is currently busy performing a context switch.
     *
     * @return true if context switch is active, false otherwise
     */
    public boolean isSwitching() {
        return remainingSwitchTicks > 0;
    }

    /**
     * Retrieves the process waiting to be loaded after context switch finishes.
     *
     * @return incoming Process
     */
    public Process getIncomingProcess() {
        return incomingProcess;
    }

    /**
     * Retrieves the process that was saved and switched out.
     *
     * @return outgoing Process
     */
    public Process getOutgoingProcess() {
        return outgoingProcess;
    }

    /**
     * Retrieves the configured context switch duration.
     *
     * @return context switch duration in ticks
     */
    public int getSwitchDuration() {
        return switchDuration;
    }
}
