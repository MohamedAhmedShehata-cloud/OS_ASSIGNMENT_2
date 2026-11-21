package main.java.os.engine;

/**
 * ============================================================================
 * OWNER: Mohamed Ahmed (SJF & Context Switch Engine)
 * RESPONSIBILITY: Manages discrete simulation time progression (Ticks: 0, 1, 2...)
 * ============================================================================
 */
public class SimulationClock {
    private int currentTime;

    /**
     * Initializes simulation clock starting at tick 0.
     */
    public SimulationClock() {
        this.currentTime = 0;
    }

    /**
     * Advances simulation clock forward by one tick.
     */
    public void tick() {
        this.currentTime++;
    }

    /**
     * Retrieves the current simulation timestamp in ticks.
     *
     * @return current clock tick
     */
    public int getCurrentTime() {
        return this.currentTime;
    }

    /**
     * Resets the clock back to tick 0.
     */
    public void reset() {
        this.currentTime = 0;
    }
}
