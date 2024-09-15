package dev.willowyx.evilcards;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        State state = new State();
        Main.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 350);
        stage.setTitle("Evil Blackjack: " + state.tagline);
        stage.setScene(scene);
        stage.show();

        // should load playeractions, which loads its own instance of blackjack
    }

    public static void main(String[] args) {
        launch();
    }
}
