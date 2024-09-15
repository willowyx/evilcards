package dev.willowyx.evilcards;

import javafx.application.Application;
import javafx.stage.Stage;

public class CardsUI extends Application {
    @Override
    public void start(Stage stage) {
        State state = new State();
        state.showAlert("Game must be initialized by Main");
    }
}
