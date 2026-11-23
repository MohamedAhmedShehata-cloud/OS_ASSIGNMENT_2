package main.java.os.input;

import java.util.List;
import main.java.os.model.Process;

/**
 * ============================================================================
 * OWNER: Youssef Samir (Data Models & Metrics)
 * RESPONSIBILITY: Factory providing programmatic test scenarios (Test Case 1 & 2)
 * ============================================================================
 */
public class TestCaseFactory {

    /**
     * Creates and returns Test Case 1 scenario with standard parameters (Q=3, CS=0).
     */
    public static TestCase getTestCase1() {

        List<Process> processes = List.of(
            new Process("P1", 0, 5, 2),
            new Process("P2", 1, 3, 1),
            new Process("P3", 2, 4, 3)
        );

        return new TestCase("Test Case 1", processes, 3, 0);
    }

    /**
     * Creates and returns Test Case 2 scenario with aging and context switch (Q=3, CS=1).
     */
    public static TestCase getTestCase2() {

        List<Process> processes = List.of(
            new Process("P1", 0, 6, 3),
            new Process("P2", 1, 4, 2),
            new Process("P3", 2, 5, 1)
        );

        return new TestCase("Test Case 2", processes, 3, 1);
    }
}