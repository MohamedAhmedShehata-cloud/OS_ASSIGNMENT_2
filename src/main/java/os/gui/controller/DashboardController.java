package os.gui.controller;

// Platform is used to safely update JavaFX UI components
// from the JavaFX Application Thread.
import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import os.gui.view.GanttChartPanel;

// Backend simulation classes.
import main.java.os.engine.SimulationEngine;
import main.java.os.input.TestCase;
import main.java.os.input.TestCaseFactory;
import main.java.os.model.Process;
import main.java.os.observer.SimulationObserver;
import main.java.os.metrics.SystemMetricsReport;
import main.java.os.scheduler.SchedulingStrategy;
import main.java.os.scheduler.SJFScheduler;
import main.java.os.scheduler.acs.ACSScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;


/**
 * DashboardController
 *
 * This class controls the JavaFX dashboard of the CPU Scheduling Simulator.
 *
 * Its main responsibilities are:
 * 1. Read the user's selected algorithm, speed, and context-switch time.
 * 2. Start and control the simulation.
 * 3. Connect the SimulationEngine with the JavaFX interface.
 * 4. Display the current simulation time and ready queues.
 * 5. Build and update the Gantt Chart.
 * 6. Calculate and display final performance metrics.
 *
 * The backend simulation classes are used without modifying their source code.
 */
public class DashboardController {

    // ========================= FXML COMPONENTS =========================

    // These fields are connected to JavaFX elements using fx:id
    // in the dashboard.fxml file.

    // Allows the user to select the scheduling algorithm.
    @FXML private ComboBox<String> algorithmComboBox;

    // Allows the user to select the simulation playback speed.
    @FXML private ComboBox<String> speedComboBox;

    // Text field used to enter the context-switch duration.
    @FXML private TextField contextSwitchField;

    // Main simulation control buttons.
    @FXML private Button startButton;
    @FXML private Button pauseButton;
    @FXML private Button resetButton;

    // Labels used to display simulation statistics.
    @FXML private Label currentTimeLabel;
    @FXML private Label completedLabel;
    @FXML private Label avgWaitingLabel;
    @FXML private Label cpuUtilizationLabel;

    // Labels showing the number of processes in each scheduling queue.
    @FXML private Label tier1CountLabel;
    @FXML private Label tier2CountLabel;

    // Container that holds the Gantt Chart component.
    @FXML private StackPane ganttChartContainer;

    // Containers used to display the processes waiting in each queue.
    @FXML private VBox tier1QueueContainer;
    @FXML private VBox tier2QueueContainer;


    // ========================= SIMULATION STATE =========================

    // True while the simulation is actively executing.
    // volatile allows the UI thread and simulation thread
    // to see changes to this variable.
    private volatile boolean simulationRunning;

    // True when the user requests a pause.
    private volatile boolean pauseRequested;

    // True when the user requests the simulation to stop.
    private volatile boolean cancelRequested;

    // Used by the simulation thread to wait while the simulation is paused.
    // The UI thread notifies this lock when the user resumes or resets.
    private final Object pauseLock = new Object();

    // Protects the Gantt Chart data when it is accessed by different threads.
    private final Object ganttLock = new Object();

    // Runs the simulation independently from the JavaFX Application Thread.
    // This prevents the dashboard from freezing during execution.
    private Thread simulationThread;

    // Stores the Process objects used in the current simulation.
    private List<Process> simulationProcesses;

    // The custom JavaFX component responsible for drawing the Gantt Chart.
    private GanttChartPanel ganttChartPanel;

    // Stores the execution intervals that will be displayed in the Gantt Chart.
    // Each segment contains a process ID, a start time, and an end time.
    private final List<GanttChartPanel.Segment> ganttSegments =
            new ArrayList<>();


    // ========================= INITIALIZATION =========================

    /**
     * Called automatically by JavaFX after loading the FXML file.
     *
     * Initializes the available algorithms and playback speeds,
     * creates the Gantt Chart component, and resets the dashboard.
     */
    @FXML
    public void initialize() {

        // Add the scheduling algorithms supported by the GUI.
        algorithmComboBox.getItems().addAll(
                "Preemptive SJF",
                "Adaptive Cloud Scheduling (ACS)"
        );

        // Select Preemptive SJF as the default algorithm.
        algorithmComboBox.setValue("Preemptive SJF");

        // Add the available simulation playback speeds.
        speedComboBox.getItems().addAll(
                "0.5x",
                "1x",
                "2x",
                "4x"
        );

        // Use normal speed by default.
        speedComboBox.setValue("1x");

        // Pause is unavailable until a simulation starts.
        pauseButton.setDisable(true);

        // Create the Gantt Chart before resetting the dashboard.
        ganttChartPanel = new GanttChartPanel();

        // Initialize all dashboard values and queue displays.
        resetDashboard();
    }


    // ========================= START / RESUME =========================

    /**
     * Handles clicks on the Start button.
     *
     * This method supports both starting a new simulation
     * and resuming an already paused simulation.
     */
    @FXML
    private void handleStart() {

        // If a simulation is paused and its thread is still alive,
        // resume the existing simulation instead of creating a new one.
        if (pauseRequested
                && simulationThread != null
                && simulationThread.isAlive()) {

            synchronized (pauseLock) {

                // Remove the pause request.
                pauseRequested = false;

                // Wake up the simulation thread waiting on pauseLock.
                pauseLock.notifyAll();
            }

            simulationRunning = true;

            // Update the buttons to reflect the resumed state.
            startButton.setDisable(true);
            startButton.setText("Start");

            pauseButton.setDisable(false);

            return;
        }

        // Do not create another simulation thread
        // if the previous simulation is still running.
        if (simulationThread != null && simulationThread.isAlive()) {
            return;
        }

        // Prevent starting if the simulation is already marked as running.
        if (simulationRunning) {
            return;
        }

        // Read and validate the context-switch duration.
        int contextSwitch;

        try {

            contextSwitch = Integer.parseInt(
                    contextSwitchField.getText().trim()
            );

            // Negative context-switch durations are invalid.
            if (contextSwitch < 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            // Highlight the input field when the entered value is invalid.
            contextSwitchField.setStyle(
                    "-fx-border-color: #EF4444; -fx-border-radius: 5;"
            );

            // Show a helpful validation message.
            contextSwitchField.setPromptText(
                    "Enter a valid number"
            );

            return;
        }

        // Remove the validation error style after valid input.
        contextSwitchField.setStyle("");

        // Read the selected algorithm and playback speed.
        String selectedAlgorithm = algorithmComboBox.getValue();
        String selectedSpeed = speedComboBox.getValue();

        // Start a new simulation using the selected settings.
        startSimulation(
                contextSwitch,
                selectedAlgorithm,
                selectedSpeed
        );
    }


    // ========================= START SIMULATION =========================

    /**
     * Prepares and starts a new simulation.
     *
     * The actual simulation is executed on a separate worker thread
     * so that JavaFX remains responsive.
     *
     * @param contextSwitch selected context-switch duration
     * @param selectedAlgorithm selected scheduling algorithm
     * @param selectedSpeed selected playback speed
     */
    private void startSimulation(
            int contextSwitch,
            String selectedAlgorithm,
            String selectedSpeed) {

        // Clear the previous simulation results before starting again.
        resetDashboard();

        // Initialize the state flags for the new simulation.
        simulationRunning = true;
        pauseRequested = false;
        cancelRequested = false;

        // Update button states while the simulation is running.
        startButton.setDisable(true);
        pauseButton.setDisable(false);

        // Prevent changing simulation settings during execution.
        algorithmComboBox.setDisable(true);
        contextSwitchField.setDisable(true);

        // Keep the Gantt Chart visible while the simulation runs.
        ganttChartContainer.getChildren().setAll(ganttChartPanel);

        // Create a background thread to execute the simulation.
        Thread worker = new Thread(
                () -> executeSimulation(
                        contextSwitch,
                        selectedAlgorithm,
                        selectedSpeed
                ),
                "OS-Simulation-Thread"
        );

        // Keep a reference to the worker so it can be paused or cancelled.
        simulationThread = worker;

        // A daemon thread does not prevent the application from closing.
        worker.setDaemon(true);

        // Start executing the simulation.
        worker.start();
    }


    // ========================= SIMULATION EXECUTION =========================

    /**
     * Creates the backend simulation and connects it to the GUI.
     *
     * This method runs on the background simulation thread.
     *
     * @param contextSwitch context-switch duration selected by the user
     * @param selectedAlgorithm selected scheduling algorithm
     * @param selectedSpeed playback speed
     */
    private void executeSimulation(
            int contextSwitch,
            String selectedAlgorithm,
            String selectedSpeed) {

        try {

            // Get a fresh test case from the backend factory.
            // A fresh test case is important because Process objects
            // are modified during simulation execution.
            TestCase originalTestCase =
                    TestCaseFactory.getTestCase1();

            // Create a new TestCase using the original processes
            // but replace the context-switch duration with the user's value.
            TestCase testCase = new TestCase(
                    originalTestCase.name(),
                    originalTestCase.processes(),
                    originalTestCase.timeQuantum(),
                    contextSwitch
            );

            // Keep a reference to all processes used in this simulation.
            simulationProcesses = testCase.processes();


            // ========================= SELECT SCHEDULER =========================

            // SchedulingStrategy is the common interface implemented
            // by the available scheduling algorithms.
            SchedulingStrategy strategy;

            if ("Adaptive Cloud Scheduling (ACS)".equals(selectedAlgorithm)) {

                // Create the backend ACS scheduler.
                strategy = new ACSScheduler(
                        "ACS",
                        testCase.timeQuantum()
                );

            } else {

                // Use a GUI adapter around the backend SJF scheduler.
                // The adapter handles a preemption issue in the current
                // SimulationEngine without modifying backend files.
                strategy = new GuiSafeSjfScheduler();
            }


            // ========================= CREATE SIMULATION ENGINE =========================

            // The SimulationEngine controls the simulation clock,
            // process execution, scheduling decisions, and context switches.
            SimulationEngine engine = new SimulationEngine(
                    strategy,
                    testCase
            );


            // ========================= TRACK PROCESS EXECUTION =========================

            /*
             * The current SimulationEngine does not provide a callback
             * that reliably identifies the process executed during a tick.
             *
             * Therefore, we store each process's remaining time before
             * execution and compare it with its value after the next tick.
             *
             * If a process's remaining time decreases, that process
             * executed during the current tick.
             *
             * IdentityHashMap is used because Process objects are tracked
             * by their object identity rather than by equals().
             */
            Map<Process, Integer> previousRemainingTimes =
                    new IdentityHashMap<>();

            // Save the initial remaining time of every process.
            for (Process process : simulationProcesses) {
                previousRemainingTimes.put(
                        process,
                        process.getRemainingTime()
                );
            }


            // ========================= REGISTER GUI OBSERVER =========================

            /*
             * The observer receives updates from SimulationEngine.
             *
             * onTick() is called after each simulation time unit.
             * We use it to update the dashboard and Gantt Chart.
             *
             * The current backend engine does not invoke all the other
             * observer callbacks, so those methods remain empty below.
             */
            engine.addObserver(new SimulationObserver() {

                /**
                 * Called after each simulation tick.
                 *
                 * @param currentTime current simulation time
                 * @param runningProcess process reported as currently running
                 * @param tier1Queue snapshot of Tier 1 ready queue
                 * @param tier2Queue snapshot of Tier 2 ready queue
                 */
                @Override
                public void onTick(
                        int currentTime,
                        Process runningProcess,
                        List<Process> tier1Queue,
                        List<Process> tier2Queue) {

                    // If the user paused the simulation,
                    // wait here until the simulation is resumed.
                    waitIfPaused();

                    // Stop execution if the user requested a reset.
                    if (cancelRequested) {
                        throw new CancellationException();
                    }

                    // Count how many processes have completed so far.
                    int completed = (int) simulationProcesses.stream()
                            .filter(Process::isCompleted)
                            .count();


                    // ========================= IDENTIFY EXECUTED PROCESS =========================

                    // The engine may change currentlyRunning before calling
                    // onTick(), so runningProcess alone is not always reliable.
                    // Compare remaining times to identify the process
                    // that actually consumed one CPU time unit.
                    Process executedProcess = null;

                    synchronized (previousRemainingTimes) {

                        for (Process process : simulationProcesses) {

                            int previousRemaining =
                                    previousRemainingTimes.get(process);

                            int currentRemaining =
                                    process.getRemainingTime();

                            // A smaller remaining time means this process
                            // executed during the current simulation tick.
                            if (currentRemaining < previousRemaining) {
                                executedProcess = process;
                            }

                            // Save the new value for comparison on the next tick.
                            previousRemainingTimes.put(
                                    process,
                                    currentRemaining
                            );
                        }
                    }


                    // ========================= UPDATE GANTT CHART DATA =========================

                    // This snapshot will be passed to JavaFX for rendering.
                    List<GanttChartPanel.Segment> chartSnapshot;

                    // Protect the shared Gantt Chart list from concurrent access.
                    synchronized (ganttLock) {

                        if (executedProcess != null) {

                            String processId = executedProcess.getId();

                            // If the previous segment belongs to the same process
                            // and ends exactly at the current time,
                            // extend it instead of creating a new segment.
                            if (!ganttSegments.isEmpty()) {

                                GanttChartPanel.Segment lastSegment =
                                        ganttSegments.get(
                                                ganttSegments.size() - 1
                                        );

                                // Merge consecutive ticks of the same process.
                                if (lastSegment.getProcessId().equals(processId)
                                        && lastSegment.getEnd() == currentTime) {

                                    ganttSegments.set(
                                            ganttSegments.size() - 1,
                                            new GanttChartPanel.Segment(
                                                    processId,
                                                    lastSegment.getStart(),
                                                    currentTime + 1
                                            )
                                    );

                                } else {

                                    // A different process executed,
                                    // so create a new Gantt Chart segment.
                                    ganttSegments.add(
                                            new GanttChartPanel.Segment(
                                                    processId,
                                                    currentTime,
                                                    currentTime + 1
                                            )
                                    );
                                }

                            } else {

                                // The first process execution creates
                                // the first Gantt Chart segment.
                                ganttSegments.add(
                                        new GanttChartPanel.Segment(
                                                processId,
                                                currentTime,
                                                currentTime + 1
                                        )
                                );
                            }
                        }

                        // Copy the list so JavaFX receives a stable snapshot
                        // instead of accessing the list while it is being modified.
                        chartSnapshot = new ArrayList<>(ganttSegments);
                    }


                    // ========================= UPDATE JAVAFX INTERFACE =========================

                    /*
                     * JavaFX UI components must only be modified
                     * from the JavaFX Application Thread.
                     *
                     * Platform.runLater() schedules the following updates
                     * on that thread.
                     */
                    Platform.runLater(() -> {

                        // Ignore pending UI updates after a reset.
                        if (cancelRequested) {
                            return;
                        }

                        // Display the current simulation time.
                        // currentTime + 1 represents the time after this tick.
                        currentTimeLabel.setText(
                                String.valueOf(currentTime + 1)
                        );

                        // Display the number of completed processes.
                        completedLabel.setText(
                                String.valueOf(completed)
                        );

                        // Refresh the Tier 1 and Tier 2 queue displays.
                        updateQueue(
                                tier1QueueContainer,
                                tier1CountLabel,
                                tier1Queue
                        );

                        updateQueue(
                                tier2QueueContainer,
                                tier2CountLabel,
                                tier2Queue
                        );

                        // Render the latest Gantt Chart snapshot.
                        if (ganttChartPanel != null) {
                            ganttChartPanel.setSegments(chartSnapshot);
                        }
                    });


                    // ========================= SIMULATION SPEED =========================

                    // Delay the worker thread to make the simulation
                    // playback visible to the user.
                    // This does not block the JavaFX Application Thread.
                    try {

                        Thread.sleep(getDelay(selectedSpeed));

                    } catch (InterruptedException e) {

                        // Restore the interrupted status of the thread.
                        Thread.currentThread().interrupt();

                        // Treat interruption as a cancellation request.
                        throw new CancellationException();
                    }
                }


                /**
                 * Callback for context-switch events.
                 *
                 * The current SimulationEngine does not call this method.
                 * It is implemented because it is required by the interface.
                 */
                @Override
                public void onContextSwitch(
                        int currentTime,
                        Process outgoingProcess,
                        Process incomingProcess) {
                    // Not invoked by the current SimulationEngine.
                }


                /**
                 * Callback for completed processes.
                 *
                 * The current engine does not notify observers through
                 * this callback, so completion is tracked using Process state.
                 */
                @Override
                public void onProcessFinished(
                        int currentTime,
                        Process completedProcess) {
                    // Completion is tracked through process state.
                }


                /**
                 * Callback for the end of the entire simulation.
                 *
                 * The current SimulationEngine does not invoke this callback.
                 */
                @Override
                public void onSimulationCompleted(
                        SystemMetricsReport report) {
                    // Not invoked by the current SimulationEngine.
                }
            });


            // ========================= RUN SIMULATION =========================

            // Start the backend simulation loop.
            // This call blocks the current worker thread until completion.
            engine.run();

            // Calculate final metrics only if the simulation was not cancelled.
            if (!cancelRequested) {
                calculateFinalMetrics();
            }

        } catch (CancellationException e) {

            // CancellationException is used to stop the simulation
            // when the user presses Reset or interrupts the worker.
            // This is an expected control-flow event, not an application error.

        } catch (Exception e) {

            // Print unexpected errors to the console for debugging.
            e.printStackTrace();

            // Show the error message in the JavaFX interface.
            Platform.runLater(() -> {

                if (!cancelRequested) {

                    ganttChartContainer.getChildren().setAll(
                            createStatusLabel(
                                    "Simulation error: " + e.getMessage()
                            )
                    );
                }
            });

        } finally {

            // Mark the simulation as no longer running.
            simulationRunning = false;

            // Keep a reference to the thread that is finishing.
            Thread currentWorker = Thread.currentThread();

            // Restore the dashboard controls on the JavaFX thread.
            Platform.runLater(() -> {

                // Avoid an old worker changing the interface
                // after a newer simulation has already started.
                if (simulationThread == currentWorker) {

                    simulationThread = null;

                    startButton.setDisable(false);
                    startButton.setText("Start");

                    pauseButton.setDisable(true);

                    algorithmComboBox.setDisable(false);
                    contextSwitchField.setDisable(false);

                    if (cancelRequested) {

                        // Clear the dashboard after cancellation.
                        resetDashboard();

                    } else {

                        // Keep the final Gantt Chart visible.
                        ganttChartContainer.getChildren().setAll(
                                ganttChartPanel
                        );
                    }
                }
            });
        }
    }


    // ========================= PAUSE / RESUME =========================

    /**
     * Blocks the simulation thread while pauseRequested is true.
     *
     * wait() releases pauseLock while waiting.
     * notifyAll() wakes the simulation thread when the user resumes.
     */
    private void waitIfPaused() {

        synchronized (pauseLock) {

            // Continue waiting while paused, unless cancellation was requested.
            while (pauseRequested && !cancelRequested) {

                try {

                    pauseLock.wait();

                } catch (InterruptedException e) {

                    // Preserve the interrupted status.
                    Thread.currentThread().interrupt();

                    // Stop the simulation.
                    throw new CancellationException();
                }
            }
        }

        // Also stop if cancellation was requested while waiting.
        if (cancelRequested) {
            throw new CancellationException();
        }
    }


    /**
     * Handles the Pause button.
     *
     * It does not terminate the simulation thread.
     * Instead, it requests the worker thread to wait at the next tick callback.
     */
    @FXML
    private void handlePause() {

        // Ignore the request if the simulation is not running
        // or is already paused.
        if (!simulationRunning || pauseRequested) {
            return;
        }

        // Ask the simulation thread to pause.
        pauseRequested = true;
        simulationRunning = false;

        // Enable Start so the user can resume the simulation.
        startButton.setDisable(false);
        startButton.setText("Resume");

        // Disable Pause while already paused.
        pauseButton.setDisable(true);
    }


    // ========================= SIMULATION SPEED =========================

    /**
     * Converts the selected playback speed into a delay in milliseconds.
     *
     * A smaller delay means the simulation runs faster.
     *
     * @param speed selected speed from the ComboBox
     * @return delay between simulation ticks in milliseconds
     */
    private long getDelay(String speed) {

        if (speed == null) {
            return 500;
        }

        switch (speed) {

            case "0.5x":
                return 1000;

            case "2x":
                return 250;

            case "4x":
                return 125;

            // Default speed is 1x.
            default:
                return 500;
        }
    }


    // ========================= RESET =========================

    /**
     * Handles the Reset button.
     *
     * Requests the current simulation to stop, wakes it if paused,
     * and restores the dashboard to its initial state.
     */
    @FXML
    private void handleReset() {

        // Tell the worker thread to stop at the next cancellation check.
        cancelRequested = true;

        // Clear pause state so the worker is not left waiting.
        pauseRequested = false;
        simulationRunning = false;

        // Wake the worker if it is currently waiting because of Pause.
        synchronized (pauseLock) {
            pauseLock.notifyAll();
        }

        // If the worker is still alive, temporarily disable Start.
        if (simulationThread != null && simulationThread.isAlive()) {

            startButton.setDisable(true);

        } else {

            // No active worker remains.
            simulationThread = null;

            startButton.setDisable(false);

            algorithmComboBox.setDisable(false);
            contextSwitchField.setDisable(false);
        }

        // Restore the button labels and states.
        startButton.setText("Start");
        pauseButton.setDisable(true);

        // Reset all displayed values and chart data.
        resetDashboard();
    }


    // ========================= FINAL METRICS =========================

    /**
     * Calculates the final performance metrics after the simulation ends.
     *
     * Waiting Time = Turnaround Time - Burst Time
     *
     * Turnaround Time = Finish Time - Arrival Time
     *
     * CPU Utilization = Total CPU Burst Time / Total Finish Time * 100
     */
    private void calculateFinalMetrics() {

        int totalWaitingTime = 0;
        int totalBurstTime = 0;
        int totalFinishTime = 0;

        int completed = 0;

        // Process every process from the current test case.
        for (Process process : simulationProcesses) {

            // Add the original CPU burst time.
            totalBurstTime += process.getBurstTime();

            // Only include processes that completed successfully.
            if (process.isCompleted()
                    && process.getFinishTime() >= 0) {

                completed++;

                // Calculate the process turnaround time.
                int turnaroundTime =
                        process.getFinishTime()
                                - process.getArrivalTime();

                // Calculate the process waiting time.
                int waitingTime =
                        turnaroundTime - process.getBurstTime();

                totalWaitingTime += waitingTime;

                // Keep the latest process finish time.
                totalFinishTime = Math.max(
                        totalFinishTime,
                        process.getFinishTime()
                );
            }
        }

        // Copy values into final local variables
        // so they can be safely used inside the JavaFX lambda.
        final int finalCompleted = completed;

        // Calculate average waiting time.
        final double averageWaitingTime =
                finalCompleted == 0
                        ? 0
                        : (double) totalWaitingTime / finalCompleted;

        // Calculate CPU utilization.
        // This formula treats elapsed time up to the last finish
        // as the total available time.
        final double cpuUtilization =
                totalFinishTime == 0
                        ? 0
                        : (double) totalBurstTime
                        / totalFinishTime * 100;

        // Update the metrics labels on the JavaFX Application Thread.
        Platform.runLater(() -> {

            completedLabel.setText(
                    String.valueOf(finalCompleted)
            );

            avgWaitingLabel.setText(
                    String.format("%.2f", averageWaitingTime)
            );

            cpuUtilizationLabel.setText(
                    String.format("%.1f%%", cpuUtilization)
            );
        });
    }


    // ========================= QUEUE DISPLAY =========================

    /**
     * Updates one queue section in the dashboard.
     *
     * @param container VBox that displays the queue processes
     * @param countLabel label displaying the number of processes
     * @param processes current queue snapshot
     */
    private void updateQueue(
            VBox container,
            Label countLabel,
            List<Process> processes) {

        // Display the current number of queued processes.
        countLabel.setText(
                processes.size() + " processes"
        );

        // Remove the previous queue contents before refreshing.
        container.getChildren().clear();

        // Show a message when the queue has no processes.
        if (processes.isEmpty()) {

            container.getChildren().add(
                    createStatusLabel("Queue is empty")
            );

            return;
        }

        // Add one label for each process in the queue.
        for (Process process : processes) {

            container.getChildren().add(
                    createStatusLabel(
                            process.getId()
                                    + "  |  Remaining: "
                                    + process.getRemainingTime()
                    )
            );
        }
    }


    // ========================= DASHBOARD RESET =========================

    /**
     * Restores all dashboard components to their initial values.
     *
     * This method is used during initialization, before a new simulation,
     * and after a simulation is cancelled.
     */
    private void resetDashboard() {

        // Reset simulation time and completion count.
        currentTimeLabel.setText("0");
        completedLabel.setText("0");

        // Reset performance metrics.
        avgWaitingLabel.setText("0.0");
        cpuUtilizationLabel.setText("0%");

        // Reset queue counters.
        tier1CountLabel.setText("0 processes");
        tier2CountLabel.setText("0 processes");

        // Clear the old queue contents.
        tier1QueueContainer.getChildren().clear();
        tier2QueueContainer.getChildren().clear();

        // Display the initial empty-queue messages.
        tier1QueueContainer.getChildren().add(
                createStatusLabel("Queue is empty")
        );

        tier2QueueContainer.getChildren().add(
                createStatusLabel("Queue is empty")
        );

        // Clear previous Gantt Chart execution intervals safely.
        synchronized (ganttLock) {
            ganttSegments.clear();
        }

        // Make sure the Gantt Chart component exists.
        if (ganttChartPanel == null) {
            ganttChartPanel = new GanttChartPanel();
        }

        // Tell the Gantt Chart to display its empty state.
        ganttChartPanel.setSegments(new ArrayList<>());

        // Keep the Gantt Chart component inside its container.
        ganttChartContainer.getChildren().setAll(
                ganttChartPanel
        );
    }


    // ========================= STATUS LABEL =========================

    /**
     * Creates a simple styled label used for queue messages and errors.
     *
     * @param message text to display
     * @return configured JavaFX Label
     */
    private Label createStatusLabel(String message) {

        Label label = new Label(message);

        // Apply the dashboard's secondary text style.
        label.setStyle(
                "-fx-text-fill: #64748B;"
                        + "-fx-font-size: 12px;"
        );

        return label;
    }


    // ========================= GUI-SAFE SJF ADAPTER =========================

    /**
     * Adapter around the existing backend SJFScheduler.
     *
     * The current SimulationEngine selects another process when
     * shouldPreempt() returns true, but it does not reinsert the
     * previously running process into the ready queue.
     *
     * This adapter addresses that specific issue for the GUI's SJF mode:
     * before returning true, it adds the preempted process back to the
     * delegate scheduler's ready queue.
     *
     * The original SJFScheduler and SimulationEngine files remain unchanged.
     */
    private static class GuiSafeSjfScheduler
            implements SchedulingStrategy {

        // The original backend SJF implementation.
        private final SJFScheduler delegate = new SJFScheduler();


        /**
         * Adds a newly arrived process to the backend ready queue.
         */
        @Override
        public void addProcess(Process process, int currentTime) {
            delegate.addProcess(process, currentTime);
        }


        /**
         * Selects and removes the next process from the ready queue.
         */
        @Override
        public Process selectNextProcess(int currentTime) {
            return delegate.selectNextProcess(currentTime);
        }


        /**
         * Forwards each simulation tick to the original scheduler.
         */
        @Override
        public void onTick(int currentTime, Process currentlyRunning) {
            delegate.onTick(currentTime, currentlyRunning);
        }


        /**
         * Checks whether the running process should be preempted.
         *
         * If preemption is required, the currently running process
         * is returned to the ready queue before the engine selects
         * the next process.
         *
         * @return true if the current process should be preempted
         */
        @Override
        public boolean shouldPreempt(
                Process currentlyRunning,
                int currentTime) {

            // Ask the original SJF scheduler whether a better candidate exists.
            boolean preempt = delegate.shouldPreempt(
                    currentlyRunning,
                    currentTime
            );

            // Reinsert the running process so it is not lost
            // when the engine switches to another process.
            if (preempt) {
                delegate.addProcess(currentlyRunning, currentTime);
            }

            return preempt;
        }


        /**
         * Returns a snapshot of the Tier 1 ready queue.
         */
        @Override
        public List<Process> getTier1Queue() {
            return delegate.getTier1Queue();
        }


        /**
         * SJF does not use a Tier 2 queue.
         */
        @Override
        public List<Process> getTier2Queue() {
            return delegate.getTier2Queue();
        }


        /**
         * Returns the scheduler's display name.
         */
        @Override
        public String getName() {
            return delegate.getName();
        }
    }
}