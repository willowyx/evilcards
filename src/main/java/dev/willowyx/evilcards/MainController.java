package dev.willowyx.evilcards;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class MainController {
    private String[] stylevariants = {"playing all by yourself, pookie?", "I want you to know that I'm never leaving",
        "happy we'll be beyond the seaaaaa", "shikanokonokonokoko...", "biblically accurate card dealer"};

    State state = new State();
    private Stage stage;

    @FXML
    private Button quitbtn, loadbtn, startbtn;

    @FXML
    private Label gamever, styletext;

    @FXML
    protected void onQuitClick() {
        exitMain();
    }

    private String randomST() {
        Random rnd = new Random();
        return stylevariants[rnd.nextInt(stylevariants.length)];
    }

    private void exitMain() {
        Stage curstage = (Stage) quitbtn.getScene().getWindow();
        curstage.close();
    }

    public void setAppIcon(String type, Stage stage) {
        switch (type) {
            case "default":
                URL url = getClass().getResource("/appicon.png");
                if (url != null) {
                    Image img_appicon = new Image(url.toString());
                    stage.getIcons().add(img_appicon);
                } else {
                    System.out.println("App icon not found in resources");
                }
                break;
        }
    }

    @FXML
    private void startSaveLoad() {
        loadbtn.setDisable(true);
        startbtn.setDisable(true);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load save");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Evil Blackjack save (*.json)", "*.json"));
        File loadfile = fileChooser.showOpenDialog(stage);

        if (loadfile == null) {
            System.out.println("No file selected");
            loadbtn.setDisable(false);
            startbtn.setDisable(false);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cards-view.fxml"));
            Parent root = loader.load();

            CardUIController cuicontroller = loader.getController();
            cuicontroller.handleSave(loadfile);

            Stage cardStage = new Stage();
            cardStage.setScene(new Scene(root));
            cardStage.setTitle("Evil Blackjack");
            cardStage.show();

            setAppIcon("default", cardStage);

            exitMain();
        } catch (IOException e) {
            e.printStackTrace();
            state.showAlert("couldn't initialize window! " + e);
            loadbtn.setDisable(false);
            startbtn.setDisable(false);
        }
    }


    @FXML
    private void onStartGame() {
        loadbtn.setDisable(true);
        startbtn.setDisable(true);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cards-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Evil Blackjack");

            setAppIcon("default", stage);
            stage.show();
        } catch (IOException e) {
            state.showAlert("couldn't initialize window! " + e);
            loadbtn.setDisable(false);
            startbtn.setDisable(false);
        }
        exitMain();
    }

    @FXML
    public void initialize() {
        gamever.setText("version " + state.version);
        styletext.setText(randomST());
    }
}
