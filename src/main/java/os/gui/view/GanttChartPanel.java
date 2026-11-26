
package os.gui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class GanttChartPanel extends VBox {

    private final HBox chartContainer;
    private final HBox timelineContainer;

    public GanttChartPanel() {

        setSpacing(12);
        setPadding(new Insets(15));

        Label title = new Label("Gantt Chart");
        title.setStyle(
                "-fx-text-fill: #F8FAFC;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;"
        );

        chartContainer = new HBox(3);
        chartContainer.setAlignment(Pos.CENTER_LEFT);

        timelineContainer = new HBox();
        timelineContainer.setAlignment(Pos.CENTER_LEFT);

        VBox chartContent = new VBox(8);
        chartContent.getChildren().addAll(
                chartContainer,
                timelineContainer
        );

        ScrollPane scrollPane = new ScrollPane(chartContent);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle(
                "-fx-background: transparent;" +
                "-fx-background-color: transparent;"
        );

        getChildren().addAll(title, scrollPane);
        setStyle("-fx-background-color: #111827;");
        
        showEmptyMessage();
    }

    public void setSegments(List<Segment> segments) {

        chartContainer.getChildren().clear();
        timelineContainer.getChildren().clear();

        if (segments == null || segments.isEmpty()) {
            showEmptyMessage();
            return;
        }

        for (Segment segment : segments) {

            int duration = segment.end - segment.start;

            if (duration <= 0) {
                continue;
            }

            double blockWidth = Math.max(duration * 55.0, 60);

            VBox block = new VBox(5);
            block.setAlignment(Pos.CENTER);
            block.setMinWidth(blockWidth);
            block.setPrefWidth(blockWidth);
            block.setPrefHeight(65);

            Label processLabel = new Label(segment.processId);
            processLabel.setStyle(
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;"
            );

            Label durationLabel = new Label(duration + " units");
            durationLabel.setStyle(
                    "-fx-text-fill: #E2E8F0;" +
                    "-fx-font-size: 10px;"
            );

            block.getChildren().addAll(
                    processLabel,
                    durationLabel
            );

            block.setStyle(
                    "-fx-background-color: " + getProcessColor(segment.processId) + ";" +
                    "-fx-background-radius: 6;"
            );

            chartContainer.getChildren().add(block);

            VBox timeMarker = new VBox();
            timeMarker.setMinWidth(blockWidth);
            timeMarker.setPrefWidth(blockWidth);

            Label startLabel = new Label(String.valueOf(segment.start));
            startLabel.setStyle(
                    "-fx-text-fill: #94A3B8;" +
                    "-fx-font-size: 10px;"
            );

            timeMarker.getChildren().add(startLabel);
            timelineContainer.getChildren().add(timeMarker);
        }

        if (!segments.isEmpty()) {

            Segment lastSegment = segments.get(segments.size() - 1);

            Label endLabel = new Label(String.valueOf(lastSegment.end));
            endLabel.setStyle(
                    "-fx-text-fill: #94A3B8;" +
                    "-fx-font-size: 10px;"
            );

            timelineContainer.getChildren().add(endLabel);
        }
    }

    private void showEmptyMessage() {

        chartContainer.getChildren().clear();
        timelineContainer.getChildren().clear();

        Label message = new Label("Gantt chart will appear here");
        message.setStyle(
                "-fx-text-fill: #64748B;" +
                "-fx-font-size: 12px;"
        );

        chartContainer.getChildren().add(message);
    }

    private String getProcessColor(String processId) {

        String[] colors = {
                "#2563EB",
                "#7C3AED",
                "#0891B2",
                "#059669",
                "#D97706",
                "#DB2777"
        };

        int index = Math.abs(processId.hashCode() % colors.length);

        return colors[index];
    }

    public static class Segment {

        private final String processId;
        private final int start;
        private final int end;

        public Segment(String processId, int start, int end) {
            this.processId = processId;
            this.start = start;
            this.end = end;
        }
    }
}
