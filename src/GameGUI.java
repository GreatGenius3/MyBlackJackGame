import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GameGUI klassen
 */
public class GameGUI extends JFrame
{
    private JButton hitButton;
    private JButton standButton;
    private JButton newGameButton;
    private JLabel dealerLabel;
    private JLabel playerLabel;
    private JLabel statusLabel;
    private JPanel dealerCardsPanel;
    private JPanel playerCardsPanel;

    Deck cardDeck;
    private List<Card> dealerHand;
    private List<Card> playerHand;
    private boolean gameOver;

    /**
     * GameGUI Konstruktor
     */
    public GameGUI()
    {
        setLayout(new BorderLayout());
        setTitle("BlackJack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(1200, 800);

        initializeComponents();
        setupLayout();
        setupEventHandlers();

        setVisible(true);
        repaint();
    }

    /**
     * initializeComponents()
     *
     * Initierar alla komponenter i programmet
     *
     */
    private void initializeComponents()
    {

        dealerLabel = new JLabel("Dealer: 0", JLabel.CENTER);
        playerLabel = new JLabel("Spelare: 0", JLabel.CENTER);
        statusLabel = new JLabel("Välkommen till Blackjack!", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));

        dealerCardsPanel = new JPanel();
        dealerCardsPanel.setOpaque(false);
        playerCardsPanel = new JPanel();
        playerCardsPanel.setOpaque(false);

        hitButton = new JButton("Ta kort");
        standButton = new JButton("Stanna");
        newGameButton = new JButton("Nytt spel");

        // Stäng av knapparna
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
    }

    /**
     * setupLayout()
     *
     * Förbereder alla layouten i vårat program
     */
    private void setupLayout()
    {
        // Dealer panelen
        JPanel dealerPanel = new JPanel();
        dealerPanel.add(dealerLabel, BorderLayout.NORTH);
        dealerPanel.add(dealerCardsPanel, BorderLayout.CENTER);

        dealerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        dealerPanel.setBorder(BorderFactory.createTitledBorder("Dealer"));
        dealerPanel.setBackground(new Color(184, 219, 128));
        dealerPanel.setPreferredSize(new Dimension(600, 400));

        // Spelar Panelen
        JPanel playerPanel = new JPanel();
        playerPanel.add(playerLabel, BorderLayout.NORTH);
        playerPanel.add(playerCardsPanel, BorderLayout.CENTER);

        playerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        playerPanel.setBorder(BorderFactory.createTitledBorder("Player"));
        playerPanel.setBackground(new Color(184, 219, 128));
        playerPanel.setPreferredSize(new Dimension(600, 300));

        // Panel med knappar
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(newGameButton);

        // Status panelen
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(statusLabel, BorderLayout.CENTER);

        // Lägg till alla paneler
        add(dealerPanel, BorderLayout.NORTH);
        add(playerPanel, BorderLayout.CENTER);

        // Skapa en container
        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.add(buttonPanel, BorderLayout.CENTER);
        bottomContainer.add(statusPanel, BorderLayout.SOUTH);

        // Lägg till kontainer
        add(bottomContainer, BorderLayout.SOUTH);

        // Försäkra om att knapparna är synliga
        hitButton.setVisible(true);
        standButton.setVisible(true);
        newGameButton.setVisible(true);
    }

    /**
     * setupEventHandlers()
     *
     * Gör iordning knapparna så dem kopplas till funktioner
     */
    private void setupEventHandlers()
    {
        hitButton.addActionListener(e -> playerHits());
        standButton.addActionListener(e -> playerStands());
        newGameButton.addActionListener(e -> startNewGame());
    }

    /**
     * initializeDeck()
     *
     * Initierar kortleken inför ett nytt spel
     */
    private void initializeDeck()
    {
        cardDeck = new Deck();
        Collections.shuffle(cardDeck.getDeck());
        dealerHand = new ArrayList<>();
        playerHand = new ArrayList<>();
        gameOver = false;

        // Rensa panelen
        dealerCardsPanel.removeAll();
        playerCardsPanel.removeAll();

        // Ge ut kort till spelaren
        // och dealern
        playerHand.add(drawCard());
        dealerHand.add(drawCard());
        playerHand.add(drawCard());

        // Uppdatera display
        updateDisplay();

        // Stäng av eller sätt igång knapparna
        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        newGameButton.setEnabled(false);

        // Kolla efter blackjack
        if (calculateHandValue(playerHand) == 21)
        {
            playerStands();
        }
    }

    /**
     * drawCard()
     *
     * Drar ett nytt kort
     *
     * @return det nya kortet
     */
    private Card drawCard()
    {
        // Är kort decken tom?
        if (cardDeck.isEmpty())
        {
            initializeDeck();
        }
        return cardDeck.remove(0);
    }

    /**
     * updateDisplay()
     *
     * Uppdaterar fälten för korten samt textdisplayen
     */
    private void updateDisplay()
    {
        // Uppdatera dealer panelen
        dealerCardsPanel.removeAll();
        for (int i = 0; i < dealerHand.size(); i++)
        {
            Card card = dealerHand.get(i);
            dealerCardsPanel.add(new CardPrinter(card));
        }

        // Uppdatera spelarens panel
        playerCardsPanel.removeAll();
        for (Card card : playerHand)
        {
            playerCardsPanel.add(new CardPrinter(card));
        }

        // Uppdatera poängen
        int dealerValue = calculateHandValue(dealerHand);
        int playerValue = calculateHandValue(playerHand);

        dealerLabel.setText("Dealer: " + dealerValue );
        playerLabel.setText("Spelare: " + playerValue);

        // Måla om UI:n
        dealerCardsPanel.revalidate();
        dealerCardsPanel.repaint();
        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();
    }

    /**
     * calculateHandValue()
     *
     * Beräknar kortvärden som finns i varje hand
     *
     * @param hand den listan på korten som handen har
     * @return poängen
     */
    private int calculateHandValue(List<Card> hand)
    {
        int score = 0;
        int aces = 0;

        for (Card c : hand)
        {
            if (c.getCardValue() == 1)
            {
                aces++;
                score += 11;
            }
            else
                score += c.getCardValue();
        }

        // Handle aces
        while (score > 21 && aces > 0)
        {
            score -= 10;
            aces--;
        }

        return score;
    }

    /**
     * getCardValue()
     *
     * @param card korten vi vill ta fram värdet
     * @return värdet från det kortet
     */
    private int getCardValue(Card card)
    {
        return card.cardValue;
    }

    /**
     * determineWinner()
     *
     * Räkna ut vem som vann spelet
     *
     */
    private void determineWinner()
    {
        int playerValue = calculateHandValue(playerHand);
        int dealerValue = calculateHandValue(dealerHand);

        if (playerValue > 21)
        {
            statusLabel.setText("Du förlorade! Du fick " + playerValue);
        }
        else if (dealerValue > 21)
        {
            statusLabel.setText("Grattis! Du vann! Banken fick " + dealerValue);
        }
        else if (playerValue > dealerValue)
        {
            statusLabel.setText("Grattis! Du vann! " + playerValue + " mot " + dealerValue);
        }
        else if (playerValue < dealerValue)
        {
            statusLabel.setText("Du förlorade! " + playerValue + " mot " + dealerValue);
        }
        else
        {
            statusLabel.setText("Oavgjort! " + playerValue + " lika med " + dealerValue);
        }
    }

    /**
     * endGame()
     *
     * Avlusta denna runda och tal även om vem som vann
     *
     * @param playerWon Vann spelaren?
     */
    private void endGame(boolean playerWon)
    {
        gameOver = true;
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        newGameButton.setEnabled(true);
    }

    /**
     * startNewGame()
     *
     * Starta nytt spel
     */
    private void startNewGame()
    {
        // Initialize deck and hands
        initializeDeck();
    }

    /**
     * playerHits()
     *
     * Spelaren trycker på ta nytt kort knappen
     */
    private void playerHits()
    {
        if (!gameOver)
        {
            playerHand.add(drawCard());
            updateDisplay();

            int playerValue = calculateHandValue(playerHand);
            if (playerValue > 21)
            {
                statusLabel.setText("Du förlorade! Du fick " + playerValue);
                endGame(false);
            }
        }
    }

    /**
     * playerStands()
     *
     * Spelaren väljer att stanna
     */
    private void playerStands()
    {
        if (!gameOver)
        {
            gameOver = true;
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            newGameButton.setEnabled(true);

            // Dealer draws cards
            while (calculateHandValue(dealerHand) < 17)
            {
                dealerHand.add(drawCard());
            }

            updateDisplay();
            determineWinner();
        }
    }
}
