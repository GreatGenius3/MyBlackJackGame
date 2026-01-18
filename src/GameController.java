import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GameController {
    Deck cardDeck;
    private List<Card> dealerHand;
    private List<Card> playerHand;
    private boolean gameOver;

    public GameController() {

    }


    Deck getCardDeck() {
        return cardDeck;
    }

    List<Card> getDealerHand() {
        return dealerHand;
    }

    List<Card> getPlayerHand() {
        return playerHand;
    }

    boolean isGameOver() {
        return gameOver;
    }

    public int getCardValue(Card card) {
        return card.cardValue;
    }

    public void addPlayerCard() {
        playerHand.add(drawCard());
    }

    public void addDealerCard() {
        dealerHand.add(drawCard());

    }

    public Card getPlayerCard(int index) {
        return getPlayerHand().get(index);
    }

    public Card getDealerCard(int index) {
        return getDealerHand().get(index);
    }

    public int getAmountPlayerCards() {
        return playerHand.size();
    }

    public int getAmountDealerCards() {
        return dealerHand.size();
    }

    public int calculatePlayerHandsValue() {
        return calculateHandValue(playerHand);
    }

    public int calculateDealerHandsValue() {
        return calculateHandValue(dealerHand);
    }

    public void startNewGame() {
        initializeDeck();
    }

    public void initializeDeck() {
        cardDeck = new Deck();
        Collections.shuffle(cardDeck.getDeck());
        dealerHand = new ArrayList<>();
        playerHand = new ArrayList<>();
        gameOver = false;
    }

    public Card drawCard() {
        if (cardDeck.isEmpty()) {
            initializeDeck();
        }
        return cardDeck.remove(0);
    }

    public int calculateHandValue(List<Card> hand) {
        int score = 0;
        int aces = 0;

        for (Card c : hand) {
            if (c.getCardValue() == 1) {
                aces++;
                score += 11;
            } else score += c.getCardValue();
        }

        while (score > 21 && aces > 0) {
            score -= 10;
            aces--;
        }

        return score;
    }

    public void endGame() {
        gameOver = true;
    }
}
