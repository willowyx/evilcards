package dev.willowyx.evilcards;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CardUIController {
    Blackjack blackjack = new Blackjack();

    @FXML
    private Button quitbtn;

    @FXML
    private Button startbtn, ac_hit, ac_stand, ac_double;

    @FXML
    private TextField wInput;

    @FXML
    private Pane dealerpane, playerpane;

    @FXML
    public TextArea game_text, log_text;

    @FXML
    public VBox actionpane;

    @FXML
    protected void onStartnew() {
//        System.out.println("Children count: " + carduic.getChildren().size());
        if (blackjack == null) {
            addlog("[Error] core logic not initialized");
            return;
        }
        if(validateWInput()) {
            blackjack.initCards();
            blackjack.dealCards();
            connectActions();
            updateUI(true);
            wInput.setDisable(true);
            startbtn.setDisable(true);
            ac_hit.setDisable(false);
            ac_stand.setDisable(false);
            ac_double.setDisable(false);
            if (!blackjack.checkScores("none").equals("incomplete")) {
                gameover_ui("none", blackjack.checkScores("none"));
            }
        } else {
            dealer_say("that's not a bet you dingus...", "neutral");
        }
    }

    public void dealer_say(String say, String emotion) {
        game_text.appendText("Dealer: " + say + "\n");

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

    public boolean validateWInput() {
        try {
            int wiVal = Integer.parseInt(wInput.getText());
            return wiVal > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    protected void onSave() {
        State state = new State();
        int pwins = blackjack.getStats("wins");
        int plose = blackjack.getStats("losses");
        int evilstg = 0;
        int dlrpity = 0;
        int casht = 0;
        String npcprog = "-1";
        String npcagr = "-1";
        String turn = "cards";
        state.saveState(pwins, plose, evilstg, dlrpity, casht, npcprog, npcagr, turn);
    }

    @FXML
    protected void onStatView() {
        System.out.println("WINS   " + blackjack.getStats("wins"));
        System.out.println("LOSSES " + blackjack.getStats("losses"));
    }

    private void gameover_ui(String type, String state) {
        startbtn.setDisable(false);
        wInput.setDisable(false);
        ac_hit.setDisable(true);
        ac_stand.setDisable(true);
        ac_double.setDisable(true);
        addlog(blackjack.handleEndState(blackjack.checkScores(type)), true);
    }

    private void connectActions() {
        ac_hit.setOnAction(e -> {
            blackjack.opt("hit");
            ac_double.setDisable(true);
            if(!blackjack.checkScores("hit").equals("incomplete")) {
                gameover_ui("hit", blackjack.checkScores("hit"));
            }
        });

        ac_stand.setOnAction(e -> {
            blackjack.opt("stand");
            blackjack.checkScores("stand");
            ac_double.setDisable(true);
            if(!blackjack.checkScores("stand").equals("incomplete")) {
                gameover_ui("stand", blackjack.checkScores("stand"));
            }
        });

        ac_double.setOnAction(e -> { // should only allow 100% addition
            blackjack.opt("double");
            blackjack.checkScores("double");
            ac_hit.setDisable(true);
            ac_double.setDisable(true);
            if(!blackjack.checkScores("hit").equals("incomplete")) {
                gameover_ui("hit", blackjack.checkScores("hit"));
            }
        });

        actionpane.addEventHandler(ActionEvent.ACTION, event -> {
            if (event.getTarget() instanceof Button actionedbtn) {
//                System.out.println("Button clicked: " + actionedbtn.getText());
                updateUI();
            }
//            System.out.println("ACTION HANDLER SET");
        });
    }

    public void updateUI() {
//        System.out.println("UPDATING UI");
        dealerpane.getChildren().clear();
        playerpane.getChildren().clear();

        animateCards(blackjack.getStateInfo("dealerhand"), dealerpane, 50, 50);
        animateCards(blackjack.getStateInfo("playerhand"), playerpane, 50, 50);


        String dlhand = Arrays.toString(new ArrayList[]{blackjack.getStateInfo("dealerhand", "orig")});
        String dlpts = String.valueOf(blackjack.getpts(blackjack.getStateInfo("dealerhand", "orig")));

        String plhand = Arrays.toString(new ArrayList[]{blackjack.getStateInfo("playerhand", "orig")});
        String plpts = String.valueOf(blackjack.getpts(blackjack.getStateInfo("playerhand", "orig")));

        addlog("DEALER " + dlhand);
        addlog(" PTS   " + dlpts);
        addlog("PLAYER " + plhand);
        addlog(" PTS   " + plpts);
        addlog("=========================");
    }

    public void updateUI(boolean newgame) {
//        System.out.println("UPDATING UI");
        dealerpane.getChildren().clear();
        playerpane.getChildren().clear();

        animateCards(blackjack.getStateInfo("dealerhand"), dealerpane, 50, 50);
        animateCards(blackjack.getStateInfo("playerhand"), playerpane, 50, 50);


        String dlhand = Arrays.toString(new ArrayList[]{blackjack.getStateInfo("dealerhand", "orig")});
        String dlpts = String.valueOf(blackjack.getpts(blackjack.getStateInfo("dealerhand", "orig")));

        String plhand = Arrays.toString(new ArrayList[]{blackjack.getStateInfo("playerhand", "orig")});
        String plpts = String.valueOf(blackjack.getpts(blackjack.getStateInfo("playerhand", "orig")));

        if(newgame) {
            addlog(("DEALER " + dlhand), true);
            addlog(" PTS   " + dlpts);
            addlog("PLAYER " + plhand);
            addlog(" PTS   " + plpts);
            addlog("=========================");
        }
    }

    private void animateCards(String[] cards, Pane targetPane, double startX, double startY) {
        double offsetX = 0;

        for (String card : cards) {
            ImageView cardView = createCardImageView(card);
            cardView.setX(startX + offsetX);
            cardView.setY(0);
            targetPane.getChildren().add(cardView);

            TranslateTransition transition = new TranslateTransition(Duration.seconds(1), cardView);
            transition.setToY(startY);
            transition.play();

            offsetX += 50;
        }
    }

    private ImageView createCardImageView(String card) {
//        System.out.println("GETTING IMAGE");
        String imagePath = Objects.requireNonNull(getClass().getResource("/playingcards/" + card + ".png")).toExternalForm();
        Image image = new Image(imagePath);
        return new ImageView(image);
    }

    @FXML
    protected void onsoftquit() {
        //should check for data-based npc actions like getting angry, etc.
    }
}
