package dev.willowyx.evilcards;

import atlantafx.base.controls.Card;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class CardsUI extends GameApplication {
    State state = new State();
    @FXML
    private Pane dealerpane;

    @FXML
    private Pane playerpane;

    @Override
    protected void initSettings(GameSettings settings) {
    }

    @Override
    protected void initUI() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cards-view.fxml"));
            Parent root = loader.load();

            FXGL.getGameScene().addUINode(root);

            this.dealerpane = (Pane) root.lookup("#dealerpane");
            this.playerpane = (Pane) root.lookup("#playerpane");

        } catch (IOException e) {
            state.showAlert("could not initialize game! " + e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
