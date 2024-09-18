package dev.willowyx.evilcards;

import com.fasterxml.jackson.databind.JsonNode;
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
    private Stage stage;

    public String pkgid = "dev.willowyx.evilcards";
    public String version = "0.4.4";
    public String minver = "0.4.4";
    public String tagline = "core systems";

    public String rSaveLocation;

    public String sGamever, sAvename, sTurn;
    public int sEvilstg, sNpcprog, sDlrpity, sNpcagr,sCasht, sPwins, sPlose;

    public void saveState(int pwins, int plose, int evilstg, int dlrpity, int casht, int npcprog, int npcagr, String savename, String turn) {
        String ftype = "evilcards-save";
        String saveid = String.valueOf(Generators.timeBasedEpochRandomGenerator().generate());


        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        ObjectNode saveData = mapper.createObjectNode();

        saveData.put("ftype", ftype);
        saveData.put("pkgid", pkgid);
        saveData.put("saveid", saveid);
        // maybe replace random gen with actual metadata for displaying time of save, etc
        saveData.put("savename", savename);
        saveData.put("gamever", version);
        saveData.put("evilstg", evilstg);
        saveData.put("npcprog", npcprog);
        saveData.put("dlrpity", dlrpity);
        saveData.put("npcagr", npcagr);
        saveData.put("cash2", casht);
        saveData.put("pwins", pwins);
        saveData.put("plose", plose);
        saveData.put("turn", turn);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save game");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Evil Blackjack save (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName("evilcards_save.json");

        File saveFile = fileChooser.showSaveDialog(stage);

        if (saveFile != null) {
            try {
                mapper.writeValue(saveFile, saveData);
                rSaveLocation = saveFile.getAbsolutePath();
                System.out.println("saved to " + rSaveLocation);
            } catch (IOException e) {
                showAlert("error saving file! " + e);
            }
        } else {
            System.out.println("user aborted save");
        }
    }

    public void saveState(int pwins, int plose, int evilstg, int dlrpity, int casht, int npcprog, int npcagr, String savename, String turn, File saveloc) {
        String ftype = "evilcards-save";
        String saveid = String.valueOf(Generators.timeBasedEpochRandomGenerator().generate());

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        ObjectNode saveData = mapper.createObjectNode();

        saveData.put("ftype", ftype);
        saveData.put("pkgid", pkgid);
        saveData.put("saveid", saveid);
        // maybe replace random gen with actual metadata for displaying time of save, etc
        saveData.put("savename", savename);
        saveData.put("gamever", version);
        saveData.put("evilstg", evilstg);
        saveData.put("npcprog", npcprog);
        saveData.put("dlrpity", dlrpity);
        saveData.put("npcagr", npcagr);
        saveData.put("cash2", casht);
        saveData.put("pwins", pwins);
        saveData.put("plose", plose);
        saveData.put("turn", turn);

        if (saveloc != null) {
            try {
                mapper.writeValue(saveloc, saveData);
                System.out.println("updated " + saveloc.getAbsolutePath());
                showAlert("updated save: " + sAvename);
            } catch (IOException e) {
                showAlert("error saving file! " + e);
            }
        } else {
            System.out.println("user aborted save");
        }
    }

    public boolean checkGameLoad() {
        return sTurn != null;
    }

    public boolean checkGameVer() {
//        System.out.println("gamever "+Double.parseDouble(sGamever));
//        return Double.parseDouble(sGamever) >= Double.parseDouble(minVer);
        return true;
    }

    public boolean handleSavedata(File savedata) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(savedata);

            sGamever = rootNode.get("gamever").asText();
            sAvename = rootNode.get("savename").asText();
            sEvilstg = rootNode.get("evilstg").asInt();
            sNpcprog = rootNode.get("npcprog").asInt();
            sDlrpity = rootNode.get("dlrpity").asInt();
            sNpcagr = rootNode.get("npcagr").asInt();
            sCasht = rootNode.get("cash2").asInt();
            sPwins = rootNode.get("pwins").asInt();
            sPlose = rootNode.get("plose").asInt();
            sTurn = rootNode.get("turn").asText();

            if (checkGameLoad()) {
                return true;
            } else {
                System.out.println("Game load check failed");
                return false;
            }
        } catch (IOException e) {
            showAlert("could not get save data!");
            return false;
        }
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
