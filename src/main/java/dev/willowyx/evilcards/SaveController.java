package dev.willowyx.evilcards;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SaveController {
    int sPwins, sPlose, sEvilstg, sDlrpity, sCasht, sNpcprog, sNpcagr;
    String sAvename, sTurn;

    @FXML
    private TextField sNameInput;

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

    @FXML
    public void onSaveInit() {
        State state = new State();
        if(!sNameInput.getText().isEmpty()) {
            sAvename = sNameInput.getText();
            state.saveState(sPwins, sPlose, sEvilstg, sDlrpity, sCasht, sNpcprog, sNpcagr, sAvename, sTurn);
            onClose();
        }
    }

    @FXML
    public void onClose() {
        Stage savestage = (Stage) sNameInput.getScene().getWindow();
        savestage.close();
    }

    @FXML
    public void initialize() { }
}
