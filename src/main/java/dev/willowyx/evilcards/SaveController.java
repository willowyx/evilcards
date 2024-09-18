package dev.willowyx.evilcards;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;

public class SaveController {
    State state = new State();

    int sPwins, sPlose, sEvilstg, sDlrpity, sCasht, sNpcprog, sNpcagr;
    String sAvename, sTurn;

    String savelocf;

    @FXML
    private TextField sNameInput;

    @FXML
    private Button savebtn;

    public void varInit(int bPwins, int bPlose, int evilstg, int dlrpity, int bCasht, int npcprog, int npcagr, String savename, String turn) {
        sPwins = bPwins;
        sPlose = bPlose;
        sEvilstg = evilstg;
        sDlrpity = dlrpity;
        sCasht = bCasht;
        sNpcprog = npcprog;
        sNpcagr = npcagr;
        sAvename = savename;
        sTurn = turn;
    }

    protected boolean loadSave() {
        MainController mainc = new MainController();

        if (savelocf == null) {
            return false;
        }
        File tsavelocf = new File(savelocf);
        mainc.startSaveLoad(tsavelocf);
        return true;
    }

    @FXML
    protected void onSaveInit() {
        if(!sNameInput.getText().isEmpty()) {
            savebtn.setDisable(true);
            sAvename = sNameInput.getText();
            state.saveState(sPwins, sPlose, sEvilstg, sDlrpity, sCasht, sNpcprog, sNpcagr, sAvename, sTurn);
            savelocf = state.rSaveLocation;
            if(!loadSave()) {
                savebtn.setDisable(false);
            } else {
                onClose();
            }
        }
    }

    @FXML
    protected void onClose() {
        Stage savestage = (Stage) sNameInput.getScene().getWindow();
        savestage.close();
    }

    @FXML
    public void initialize() { }
}
