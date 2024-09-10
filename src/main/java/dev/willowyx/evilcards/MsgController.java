package dev.willowyx.evilcards;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Pos;

public class MsgController {

    @FXML
    private VBox messageContainer;

    @FXML
    private ComboBox<String> textSelect;

    @FXML
    private ScrollPane msgScrollPane;

    @FXML
    protected void handleSendMessage() {
        String messageText = textSelect.getValue();
        if (!messageText.isEmpty()) {
            addMessage(messageText, true);
            textSelect.valueProperty().set(null);
        }
    }

    public void addMessage(String message, boolean sent) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(sent ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().addAll("message-bubble", sent ? "sent" : "received");

        messageBox.getChildren().add(messageLabel);
        messageContainer.getChildren().add(messageBox);

        msgScrollPane.layout();
        msgScrollPane.setVvalue(1.0);
    }
}
