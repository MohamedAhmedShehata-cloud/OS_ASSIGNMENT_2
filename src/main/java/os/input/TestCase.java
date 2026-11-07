package os.input;

import os.model.Process;
import java.util.List;

/**
 * ============================================================================
 * OWNER: Youssef Samir (Data Models & Metrics)
 * RESPONSIBILITY: Data model encapsulating test case inputs (processes, Q, CS)
 * ============================================================================
 *
 * @param name title of the test scenario
 * @param processes initial list of processes in the scenario
 * @param timeQuantum round robin time quantum (Q)
 * @param contextSwitchTime context switch duration overhead (CS)
 */
public record TestCase(
    String name,
    List<Process> processes,
    int timeQuantum,
    int contextSwitchTime
) {}