package main.java.os.model;

/**
 * ============================================================================
 * OWNER: Youssef Samir (Data Models & Metrics)
 * RESPONSIBILITY: Defines the current scheduling tier of a process in ACS
 * ============================================================================
 */
public enum SchedulerTier {
    /** Used when scheduling outside tiered schedulers (e.g., standard SJF). */
    NONE,

    /** High-priority Round Robin queue (Tier 1). */
    TIER_1,

    /** Lower-priority Non-Preemptive Priority queue with aging (Tier 2). */
    TIER_2
}