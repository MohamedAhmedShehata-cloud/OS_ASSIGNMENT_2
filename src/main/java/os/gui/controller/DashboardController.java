
package os.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DashboardController {

    @FXML
    private ComboBox<String> algorithmComboBox;

    @FXML
    private ComboBox<String> speedComboBox;

    @FXML
    private TextField contextSwitchField;

    @FXML
    private Button startButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button resetButton;

    @FXML
    private Label currentTimeLabel;

    @FXML
    private Label completedLabel;

    @FXML
    private Label avgWaitingLabel;

    @FXML
    private Label cpuUtilizationLabel;

    @FXML
    private Label tier1CountLabel;

    @FXML
    private Label tier2CountLabel;

    @FXML
    private StackPane ganttChartContainer;

    @FXML
    private VBox tier1QueueContainer;

    @FXML
    private VBox tier2QueueContainer;

    private boolean simulationRunning = false;

    @FXML
    public void initialize() {

        algorithmComboBox.getItems().addAll(
                "Preemptive SJF",
                "Adaptive Cloud Scheduling (ACS)"
        );

        algorithmComboBox.setValue("Preemptive SJF");

        speedComboBox.getItems().addAll(
                "0.5x",
                "1x",
                "2x",
                "4x"
        );

        speedComboBox.setValue("1x");

        pauseButton.setDisable(true);

        resetDashboard();
    }

    @FXML
    private void handleStart() {

        if (simulationRunning) {
            return;
        }

        try {
            int contextSwitch = Integer.parseInt(
                    contextSwitchField.getText().trim()
            );

            if (contextSwitch < 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            contextSwitchField.setStyle(
                    "-fx-border-color: #EF4444; -fx-border-radius: 5;"
            );
            contextSwitchField.setPromptText("Enter a valid number");
            return;
        }

        contextSwitchField.setStyle("");

        simulationRunning = true;

        startButton.setDisable(true);
        pauseButton.setDisable(false);
        algorithmComboBox.setDisable(true);

        ganttChartContainer.getChildren().setAll(
                createStatusLabel("Simulation started — waiting for engine integration")
        );
    }

    @FXML
    private void handlePause() {

        if (!simulationRunning) {
            return;
        }

        simulationRunning = false;

        startButton.setDisable(false);
        pauseButton.setDisable(true);

        ganttChartContainer.getChildren().setAll(
                createStatusLabel("Simulation paused")
        );
    }

    @FXML
    private void handleReset() {

        simulationRunning = false;

        startButton.setDisable(false);
        pauseButton.setDisable(true);
        algorithmComboBox.setDisable(false);

        resetDashboard();
    }

    private void resetDashboard() {

        currentTimeLabel.setText("0");
        completedLabel.setText("0");
        avgWaitingLabel.setText("0.0");
        cpuUtilizationLabel.setText("0%");

        tier1CountLabel.setText("0 processes");
        tier2CountLabel.setText("0 processes");

        tier1QueueContainer.getChildren().clear();
        tier2QueueContainer.getChildren().clear();

        tier1QueueContainer.getChildren().add(
                createStatusLabel("Queue is empty")
        );

        tier2QueueContainer.getChildren().add(
                createStatusLabel("Queue is empty")
        );

        ganttChartContainer.getChildren().setAll(
                createStatusLabel("Gantt chart will appear here")
        );
    }

    private Label createStatusLabel(String message) {

        Label label = new Label(message);

        label.setStyle(
                "-fx-text-fill: #64748B;" +
                "-fx-font-size: 12px;"
        );

        return label;
    }
}
