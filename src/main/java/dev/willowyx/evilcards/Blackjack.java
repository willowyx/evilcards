package dev.willowyx.evilcards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Blackjack {

    private ArrayList<String> cards;
    private ArrayList<String> dealerhand;
    private ArrayList<String> playerhand;
    public int pwins;
    public int plose;
    public int casht; // total player cash, populated from json
    public int bamt; // intended bet amt

    public void initCards() {
        String[] suits = {"c", "s", "h", "d"};
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        cards = new ArrayList<>();
        dealerhand = new ArrayList<>();
        playerhand = new ArrayList<>();

        for (String suit : suits) {
            for (String value : values) {
                cards.add(value + suit);
            }
        }

        Collections.shuffle(cards);
    }

    private void addCard(ArrayList<String> hand) {
        hand.add(cards.removeFirst());
    }

    public void dealCards() {
        addCard(dealerhand);
        addCard(playerhand);
        addCard(playerhand);
//        displayTable();
        System.out.println("SHOULD UPDATE");
//        handleEndState(checkScores("none"));
    }

//    private void displayTable() {
//        cuic.addlog("DEALER " + dealerhand);
//        cuic.addlog(" PTS   " + getpts(dealerhand));
//        cuic.addlog("PLAYER " + playerhand);
//        cuic.addlog(" PTS   " + getpts(playerhand));
//        cuic.addlog("=========================");
//    }

    public int getStats(String type) {
        return switch (type) {
            case "wins" -> pwins;
            case "losses" -> plose;
            default -> -1;
        };
    }

    public int getpts(ArrayList<String> hand) {
        Pattern suitptn = Pattern.compile("[cdhs]");
        int points = 0;
        int aces = 0;

        for (String card : hand) {
            String value = suitptn.matcher(card).replaceAll("");
            switch (value) {
                case "A":
                    aces++;
                    points += 11;
                    break;
                case "J":
                case "Q":
                case "K":
                    points += 10;
                    break;
                default:
                    points += Integer.parseInt(value);
            }
        }
        while (points > 21 && aces > 0) {
            points -= 10;
            aces--;
        }
        return points;
    }

    public String[] getStateInfo(String type) {
        String[] rtnarray;
        return switch (type) {
            case "playerhand" -> {
                rtnarray = playerhand.toArray(new String[0]);
                yield rtnarray;
            }
            case "dealerhand" -> {
                rtnarray = dealerhand.toArray(new String[0]);
                yield rtnarray;
            }
            case "deck" -> {
                rtnarray = cards.toArray(new String[0]);
                yield rtnarray;
            }
            default -> {
                rtnarray = new String[]{};
                yield rtnarray;
            }
        };
    }

    public ArrayList<String> getStateInfo(String type, String orig) {
        return switch (type) {
            case "playerhand" -> playerhand;
            case "dealerhand" -> dealerhand;
            case "deck" -> cards;
            default -> null;
        };
    }

    public String checkScores(String action) {
        int dealerPts = getpts(dealerhand);
        int playerPts = getpts(playerhand);

        return switch (action) {
            case "stand" -> evalfscore(dealerPts, playerPts);
            case "hit" -> {
                if (playerPts > 21) yield "lose_bust";
                yield "incomplete";
            }
            default -> evalintscore(dealerPts, playerPts);
        };
    }

    public String bset(int amount, String action) {
        if(action.equals("normal")) {
            if(casht >= amount) {
                casht -= amount;
                bamt = amount;
                return "normal";
            } else {
                return "nofunds";
            }
        } else if(action.equals("double")) {
            if(casht >= amount) {
                casht -= amount;
                bamt += amount;
                return "double";
            }
        }
        return "noaction";
    }

    public boolean bman(String result) {
        switch (result) {
            case "win" -> {
                bamt *= 2;
                casht += bamt;
                return true;
            }
            case "tie" -> {
                casht += bamt;
                return true;
            }
            case "lose" -> {
                bamt = 0;
                return true;
            }
            case "romantical" -> {
                casht = 0;
                return true;
            }
        }
        return false;
    }

    private String evalfscore(int dealerPts, int playerPts) {
        String result = "lose_generic";
        if (dealerPts > 21) {
            result = "win_bust";
        } else if (dealerPts == playerPts) {
            result = "tie_push";
        } else if (playerPts > dealerPts) {
            result = "win_generic";
        }
        return result;
    }

    private String evalintscore(int dealerPts, int playerPts) { // some end states are slightly broken
        if (dealerPts == 21 && dealerhand.size() == 2) {
            if (playerPts == 21 && playerhand.size() == 2) {
                return "tie_blackjack";
            } else {
                return "lose_blackjack";
            }
        } else if (playerPts == 21 && playerhand.size() == 2) {
            return "win_blackjack";
        } else if (playerPts > 21) {
            return "lose_bust";
        } else if (dealerPts > 21) {
            return "win_bust";
        } else {
            return "incomplete";
        }
    }

    public void opt(String action) {
        switch (action) {
            case "hit":
                addCard(playerhand);
                break;
            case "stand":
                while (getpts(dealerhand) < 17) {
                    addCard(dealerhand);
//                    displayTable();
                    System.out.println("SHOULD UPDATE");
                }
                break;
            case "double":
                addCard(playerhand);
                break;
        }
//        displayTable();
        System.out.println("SHOULD UPDATE");
        handleEndState(checkScores(action));
    }

    public String handleEndState(String state) {
        switch (state) {
            case "tie_blackjack":
                return "TIE: Both Blackjack";
            case "lose_blackjack":
                plose += 1;
                return "YOU LOSE: Dealer Blackjack";
            case "win_blackjack":
                pwins += 1;
                return "YOU WIN: Blackjack";
            case "lose_bust":
                plose += 1;
                return "YOU LOSE: Player bust";
            case "win_bust":
                pwins += 1;
                return "YOU WIN: Dealer bust";
            case "win_generic":
                pwins += 1;
                return "YOU WIN";
            case "lose_generic":
                plose += 1;
                return "YOU LOSE";
            case "tie_push":
                return "TIE: push";
            case "incomplete":
                break;
        }
        return "Unknown game state";
    }

    public static void main(String[] args) {
        Blackjack game = new Blackjack();
        game.initCards();
        game.dealCards();

        Scanner scanner = new Scanner(System.in);
        String gameState = game.checkScores("none");

        while (gameState.equals("incomplete")) {
            System.out.print("Enter action: ");
            String action = scanner.nextLine();
            game.opt(action);
            gameState = game.checkScores(action);
        }

        System.out.println("Game over!");
    }
}
