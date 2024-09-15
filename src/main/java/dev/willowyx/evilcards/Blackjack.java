package dev.willowyx.evilcards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class Blackjack {

    private ArrayList<String> cards;
    private ArrayList<String> dealerhand;
    private ArrayList<String> playerhand;
    private int pwins;
    private int plose;
    private int casht; // total player cash, populated from json
    private int bamt; // intended bet amt

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

    public void addCard(ArrayList<String> hand) {
        hand.add(cards.removeFirst());
    }

    public int getStats(String type) {
        return switch (type) {
            case "pwins" -> pwins;
            case "plose" -> plose;
            case "casht" -> casht;
            default -> -1;
        };
    }

    public void setStats(String type, int newval) {
        switch (type) {
            case "pwins" -> pwins = newval;
            case "plose" -> plose = newval;
            case "casht" -> casht = newval;
        }
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

    public ArrayList<String> getStateInfoOrig(String type, String orig) {
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
        switch (action) {
            case "normal":
                if (casht >= amount) {
                    casht -= amount;
                    bamt = amount;
                    return "normal";
                } else {
                    return "nofunds";
                }
            case "double":
                if (casht >= amount) {
                    casht -= amount;
                    bamt += amount;
                    return "double";
                } else {
                    return "nofunds";
                }
        }
        return "noaction";
    }

    public void bman(String result) {
        switch (result) {
            case "win" -> {
                bamt *= 2;
                casht += bamt;
            }
            case "tie" -> casht += bamt;
            case "lose" -> bamt = 0;
            case "romantical" -> casht = 0;
        }
    }

    private String evalfscore(int dealerPts, int playerPts) {
        String result = "lose_generic";
        if (dealerPts > 21) {
            result = "win_bust";
        } else if (dealerPts == 21 && dealerhand.size() == 2) {
            return "lose_blackjack";
        } else if (dealerPts == playerPts) {        // only when player selects stand
            result = "tie_push";
        } else if (playerPts > dealerPts) {
            result = "win_generic";
        }
        return result;
    }

    private String evalintscore(int dealerPts, int playerPts) {
        if (playerPts == 21 && playerhand.size() == 2) {
            addCard(dealerhand);
            if (dealerPts == 21 && dealerhand.size() == 2) {
                return "tie_blackjack";
            } else {
                return "win_blackjack";
            }
        } else if (dealerPts == 21 && dealerhand.size() == 2) {
            return "lose_blackjack";
        } else if (playerPts > 21) {
            return "lose_bust";
        } else if (dealerPts > 21) {
            return "win_bust";
        } else {
            return "incomplete";
        }
    }

    public String handleEndState(String state) {
        switch (state) {
            case "tie_blackjack":
                return "TIE: Two Blackjacks";
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
                return "PUSH: You tied";
            case "incomplete":
                break;
        }
        return "Unknown game state";
    }

    public static void main(String[] args) {
        State state = new State();
        state.showAlert("Game core must be initialized by CardUI");
    }
}
