package dev.willowyx.evilcards;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.Objects;

public class CardUIController {
    Blackjack blackjack = new Blackjack();
    State state = new State();

    private int evilstg = 0;
    private int dlrpity = 0;
    private int npcprog = -1;
    private int npcagr = -1;
    private String turn = "cards";
    private File cSavefile;

    @FXML
    private Button quitbtn;

    @FXML
    private Button checkblbtn, btinitbtn, plstatsbtn, savebtn, startbtn, ac_hit, ac_stand, ac_double;

    @FXML
    private TextField wInput;

    @FXML
    private Pane dealerpane, playerpane;

    @FXML
    private TextArea game_text, log_text;

    @FXML
    public HBox actionpane;

    public void handleSave(File savefile) {
        cSavefile = savefile;
    }

    public void handleInitVar() {
        System.out.println("received as " + cSavefile);
        if (state.handleSavedata(cSavefile)) {
            if (state.checkGameVer()) {
                blackjack.setStats("pwins", state.sPwins);
                blackjack.setStats("plose", state.sPlose);
                evilstg = state.sEvilstg;
                dlrpity = state.sDlrpity;
                blackjack.setStats("casht", state.sCasht);
                npcprog = state.sNpcprog;
                npcagr = state.sNpcagr;
                turn = state.sTurn;
            } else {
                System.out.println("you are loading an old save, some things may break!");
            }
        }
    }

    @FXML
    protected void onStartnew() {
        handleInitVar();
        btinitbtn.setDisable(false);
        checkblbtn.setDisable(false);
        plstatsbtn.setDisable(false);
        startbtn.setDisable(true);
        savebtn.setDisable(false);
        dealer_say("hey, welcome. you know how to play, right?", "neutral");
        player_say("...");

//        blackjack.initCards();
//        dealCards();
    }

    public void dealCards() {
        blackjack.addCard(blackjack.getStateInfoOrig("dealerhand", "true"));
        updateUI();
        for (int i = 0; i < 2; i++) {
            blackjack.addCard(blackjack.getStateInfoOrig("playerhand", "true"));
            updateUI();
        }
    }


    public void dealer_say(String say, String emotion) {
        game_text.appendText("Dealer: " + say + "\n");
        addlog("dealer is now " + emotion);
    }

    public void player_say(String say) {
        game_text.appendText("You: " + say + "\n");
    }

    public void addlog(String text) {
        log_text.appendText(text + "\n");
    }

    public void addlog(String text, boolean overwrite) {
        log_text.setText(text + "\n");
    }

    private boolean validateWInput() {
        try {
            int wiVal = Integer.parseInt(wInput.getText());
            return wiVal > 0;
        } catch (NumberFormatException e) {
            dealer_say("that's not a bet you dingus...", "annoyed");
            return false;
        }
    }

    public boolean checkBalance(String type) {
        if(type.equals("start")) {
            String bResult = blackjack.bset(Integer.parseInt(wInput.getText()), "normal");
            return switch (bResult) {
                case "normal" -> true;
                case "nofunds" -> {
                    dealer_say("you don't have the money...", "annoyed");
                    yield false;
                }
                default -> false;
            };
        } else if(type.equals("double")) {
            String bDouble = blackjack.bset(Integer.parseInt(wInput.getText()), "double");
            return switch (bDouble) {
                case "double" -> true;
                case "nofunds" -> {
                    dealer_say("you don't have the money...", "annoyed");
                    yield false;
                }
                default -> false;
            };
        }
        return false;
    }

    @FXML
    protected void onSave() {
        int bPwins = blackjack.getStats("pwins");
        int bPlose = blackjack.getStats("plose");
        int bCasht = blackjack.getStats("casht");
        state.saveState(bPwins, bPlose, evilstg, dlrpity, bCasht, npcprog, npcagr, turn);
    }

    @FXML
    protected void onStatView() {
        addlog("WINS   " + blackjack.getStats("pwins"), true);
        addlog("LOSSES " + blackjack.getStats("plose"));
    }

    @FXML
    protected void oncheckbl() {
        String rtnbal;
        if(blackjack.getStats("casht") == 0) {
            rtnbal = "ha... you're broke.";
            dealer_say(rtnbal, "pleased");
        } else {
            rtnbal = "you have, uh, " + blackjack.getStats("casht") + " creds to spend...";
            dealer_say(rtnbal, "neutral");
        }
    }

    @FXML
    protected void onbtinit() {
        connectActions();
        if(validateWInput() && checkBalance("start")) {
            blackjack.initCards();
            dealCards();
            updateUI();
            wInput.setDisable(true);
            btinitbtn.setDisable(true);
            ac_hit.setDisable(false);
            ac_stand.setDisable(false);
            ac_double.setDisable(false);
            if (!blackjack.checkScores("none").equals("incomplete")) {
                gameover_ui("none", blackjack.checkScores("none"));
            }
        }
    }

    private void gameover_ui(String type, String state) {
        switch (state) {
            case "lose_blackjack", "lose_bust", "lose_generic":
                blackjack.bman("lose");
                break;
            case "win_blackjack", "win_bust", "win_generic":
                blackjack.bman("win");
                break;
            case "tie_blackjack", "tie_push":
                blackjack.bman("tie");
                break;
        }

        btinitbtn.setDisable(false);
        wInput.setDisable(false);
        ac_hit.setDisable(true);
        ac_stand.setDisable(true);
        ac_double.setDisable(true);
    }

    private void connectActions() {
        ac_hit.setOnAction(e -> {
            opt("hit");
            ac_double.setDisable(true);
            if(!blackjack.checkScores("hit").equals("incomplete")) {
                gameover_ui("hit", blackjack.checkScores("hit"));
            }
        });

        ac_stand.setOnAction(e -> {
            opt("stand");
            String result = blackjack.checkScores("stand");
            ac_double.setDisable(true);
            if (!result.equals("incomplete")) {
                gameover_ui("stand", result);
            }
        });


        ac_double.setOnAction(e -> {        // should only allow 100% addition
            if(checkBalance("double")) {
                opt("double");
                blackjack.checkScores("double");
                ac_hit.setDisable(true);
                ac_double.setDisable(true);
                if (!blackjack.checkScores("hit").equals("incomplete")) {
                    gameover_ui("hit", blackjack.checkScores("hit"));
                }
            }
        });

        actionpane.addEventHandler(ActionEvent.ACTION, event -> {
            if (event.getTarget() instanceof Button actionedbtn) {
//                System.out.println("Button clicked: " + actionedbtn.getText());
                updateUI();
            }
        });
    }

    public void updateUI() {
        dealerpane.getChildren().clear();
        playerpane.getChildren().clear();

        animateCards(blackjack.getStateInfo("dealerhand"), dealerpane, 50, 50, 0.3, 35);
        animateCards(blackjack.getStateInfo("playerhand"), playerpane, 50, 50, 0.3, 170);

//        String dlhand = Arrays.toString(new ArrayList[]{blackjack.getStateInfoOrig("dealerhand", "orig")});
//        String dlpts = String.valueOf(blackjack.getpts(blackjack.getStateInfoOrig("dealerhand", "orig")));
//
//        String plhand = Arrays.toString(new ArrayList[]{blackjack.getStateInfoOrig("playerhand", "orig")});
//        String plpts = String.valueOf(blackjack.getpts(blackjack.getStateInfoOrig("playerhand", "orig")));

//        addlog("DEALER " + dlhand);
//        addlog(" PTS   " + dlpts);
//        addlog("PLAYER " + plhand);
//        addlog(" PTS   " + plpts);
//        addlog("=========================");
    }

    public void opt(String action) {
        switch (action) {
            case "hit":
                blackjack.addCard(blackjack.getStateInfoOrig("playerhand", "true"));
                blackjack.handleEndState(blackjack.checkScores("hit"));
                break;
            case "stand":
                while (blackjack.getpts(blackjack.getStateInfoOrig("dealerhand", "true")) < 17) {
                    blackjack.addCard(blackjack.getStateInfoOrig("dealerhand", "true"));
                    updateUI();
                }

                Platform.runLater(() -> {
                    blackjack.handleEndState(blackjack.checkScores("stand"));
                });
                break;
            case "double":
                blackjack.addCard(blackjack.getStateInfoOrig("playerhand", "true"));
                blackjack.handleEndState(blackjack.checkScores(action));
                break;
        }
    }


    private void animateCards(String[] cards, Pane targetPane, double startX, double startY, double scale, double spacing) {
        double offsetX = 0;
//        double scale = 0.3;
//        double spacing = 170;

        for (String card : cards) {
            ImageView cardView = createCardImageView(card);

            cardView.setFitWidth(cardView.getImage().getWidth() * scale);
            cardView.setFitHeight(cardView.getImage().getHeight() * scale);

            cardView.setX(startX + offsetX);
//            cardView.setY(0);                     transition currently off
            cardView.setY(startY);
            targetPane.getChildren().add(cardView);

//            TranslateTransition transition = new TranslateTransition(Duration.seconds(1), cardView);
//            transition.setToY(startY);
//            transition.play();

            offsetX += spacing;
        }
    }

    private ImageView createCardImageView(String card) {
        String imagePath = Objects.requireNonNull(getClass().getResource("/playingcards/" + card + ".png")).toExternalForm();
        Image image = new Image(imagePath);
        return new ImageView(image);
    }

    @FXML
    protected void onsoftquit() {
        //should check for data-based npc actions like getting angry, etc.
    }
}
