
package os.gui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class QueueStatePanel extends VBox {

    private final VBox processContainer;
    private final Label queueNameLabel;
    private final Label processCountLabel;

    public QueueStatePanel(String queueName) {

        setSpacing(12);
        setPadding(new Insets(15));
        setMinWidth(200);

        queueNameLabel = new Label(queueName);
        queueNameLabel.setStyle(
                "-fx-text-fill: #F8FAFC;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;"
        );

        processCountLabel = new Label("0 processes");
        processCountLabel.setStyle(
                "-fx-text-fill: #94A3B8;" +
                "-fx-font-size: 11px;"
        );

        HBox header = new HBox(
                queueNameLabel,
                processCountLabel
        );

        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(10);

        HBox.setHgrow(processCountLabel, Priority.ALWAYS);

        processContainer = new VBox(8);
        processContainer.setPadding(new Insets(5, 0, 0, 0));

        setStyle(
                "-fx-background-color: #111827;" +
                "-fx-background-radius: 10;"
        );

        getChildren().addAll(header, processContainer);

        setQueue(List.of());
    }

    public void setQueue(List<String> processes) {

        processContainer.getChildren().clear();

        if (processes == null || processes.isEmpty()) {
            processCountLabel.setText("0 processes");

            Label emptyLabel = new Label("Queue is empty");
            emptyLabel.setStyle(
                    "-fx-text-fill: #64748B;" +
                    "-fx-font-size: 12px;"
            );

            processContainer.getChildren().add(emptyLabel);
            return;
        }

        processCountLabel.setText(
                processes.size() + " processes"
        );

        for (String processId : processes) {

            Label processLabel = new Label(processId);

            processLabel.setStyle(
                    "-fx-text-fill: #E2E8F0;" +
                    "-fx-font-size: 12px;"
            );

            HBox processCard = new HBox(processLabel);
            processCard.setAlignment(Pos.CENTER_LEFT);
            processCard.setPadding(new Insets(10));

            processCard.setStyle(
                    "-fx-background-color: #1E293B;" +
                    "-fx-background-radius: 6;"
            );

            processContainer.getChildren().add(processCard);
        }
    }
}
