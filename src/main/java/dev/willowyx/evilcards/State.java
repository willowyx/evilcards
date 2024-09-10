package dev.willowyx.evilcards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.uuid.Generators;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.IOException;

public class State {
    public String pkgid = "dev.willowyx.evilcards";
    public String version = "0.3.0";
    public String tagline = "it almost even works!!";

    private Stage primaryStage;

    public State() {
        this.primaryStage = primaryStage;
    }

    public void createSave(int pwins, int plose, int evilstg, int dlrpity, int casht, String npcprog, String npcagr, String turn) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        ObjectNode saveData = mapper.createObjectNode();
        saveData.put("ftype", "evilcards-save");
        saveData.put("pkgid", this.pkgid);
        saveData.put("saveid", String.valueOf(Generators.timeBasedEpochRandomGenerator().generate()));
        // maybe replace random gen with actual metadata for displaying time of save, etc
        saveData.put("gamever", this.version);
        saveData.put("evilstg", evilstg);
        saveData.put("npcprog", npcprog);
        saveData.put("dlrpity", dlrpity);
        saveData.put("npcagr", npcagr);
        saveData.put("cash2", casht);
        saveData.put("pwins", pwins);
        saveData.put("plose", plose);
        saveData.put("turn", turn);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Evil Blackjack - save game state");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setInitialFileName("evilcards_save.json");

        File saveFile = fileChooser.showSaveDialog(primaryStage);

        if (saveFile != null) {
            try {
                mapper.writeValue(saveFile, saveData);
                System.out.println("Saved at " + saveFile.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Encountered error saving: " + e);
            }
        } else {
            System.out.println("abort");
        }

    }

    public void saveState(int pwins, int plose, int evilstg, int dlrpity, int casht, String npcprog, String npcagr, String turn) {
        String ftype = "evilcards-save";
        String saveid = String.valueOf(Generators.timeBasedEpochRandomGenerator().generate());

        System.out.println("=== SAVING DATA ===");
        System.out.println("FTYPE   " + ftype);
        System.out.println("PKGID   " + pkgid);
        System.out.println("SAVEID  " + saveid);
        System.out.println("GAMEVER " + version);
        System.out.println("EVILSTG " + evilstg);
        System.out.println("NPCPROG " + npcprog);
        System.out.println("DLRPITY " + dlrpity);
        System.out.println("NPCAGR  " + npcagr);
        System.out.println("CASH2   " + casht);
        System.out.println("PWINS   " + pwins);
        System.out.println("PLOSE   " + plose);
        System.out.println("TURN    " + turn); // cards, destruction, text-interact, etc

        System.out.print("STATUS  ");
        createSave(pwins, plose, evilstg, dlrpity, casht, npcprog, npcagr, turn);
    }

    public String parsePrefs(String object) {
        // if nobet, disable double, maybe hide wager entirely
        return object;
    }

    public void showAlert(String content) {
        try {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
                alert.showAndWait();
            });
        } catch (IllegalStateException e) {
            System.out.println(content);
        }
    }
}
