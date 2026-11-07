package os.scheduler.acs;

import os.model.Process;

/**
 * ============================================================================
 * OWNER: Mohamed Mamdouh (Architect & ACS Engine)
 * RESPONSIBILITY: Implements aging in Tier 2: Floor(WaitTime / 4) priority boost
 * ============================================================================
 */
public class AgingPolicy {

    /**
     * Calculates the number of priority boost units based on waiting time in Tier 2: floor(WaitTime / 4).
     *
     * @param waitTimeInTier2 time duration spent waiting in Tier 2
     * @return aging priority boost units
     */
    public static int calculateAgingUnits(int waitTimeInTier2) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Applies aging formula to update effective priority of a process waiting in Tier 2.
     *
     * @param p process to evaluate and age
     * @param currentTime current simulation timestamp
     */
    public static void applyAging(Process p, int currentTime) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}