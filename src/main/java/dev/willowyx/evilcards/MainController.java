package dev.willowyx.evilcards;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    private String[] stylevariants = {"playing all by yourself, pookie?", "I want you to know that I'm never leaving",
        "happy we'll be beyond the seaaaaa"};
    State state = new State();
    @FXML
    private Button quitbtn;

    @FXML
    protected void onQuitClick() {
        Stage stage = (Stage) quitbtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onStartGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cards-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Evil Blackjack");
//            filedata.setAppIcon("default", stage);
            stage.show();
        } catch (IOException e) {
            state.showAlert("couldn't initialize window! " + e);
//            e.printStackTrace();
        }
        Stage stage = (Stage) quitbtn.getScene().getWindow();
        stage.close();
    }
}
