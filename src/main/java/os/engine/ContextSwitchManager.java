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
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Evaluates whether a context switch overhead is necessary between outgoing and incoming processes.
     *
     * @param outgoing process leaving CPU
     * @param incoming process entering CPU
     * @return true if context switch is required, false otherwise
     */
    public boolean isSwitchRequired(Process outgoing, Process incoming) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Initiates a context switch overhead period between outgoing and incoming processes.
     *
     * @param outgoing process being switched out
     * @param incoming process to be loaded next
     */
    public void startSwitch(Process outgoing, Process incoming) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Decrements remaining context switch overhead ticks by one.
     */
    public void tick() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Checks if CPU is currently busy performing a context switch.
     *
     * @return true if context switch is active, false otherwise
     */
    public boolean isSwitching() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Retrieves the process waiting to be loaded after context switch finishes.
     *
     * @return incoming Process
     */
    public Process getIncomingProcess() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Retrieves the process that was saved and switched out.
     *
     * @return outgoing Process
     */
    public Process getOutgoingProcess() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Retrieves the configured context switch duration.
     *
     * @return context switch duration in ticks
     */
    public int getSwitchDuration() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}