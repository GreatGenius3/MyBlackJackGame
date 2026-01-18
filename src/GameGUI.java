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

    GameController gameController;

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

        gameController = new GameController();

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
     * updateDisplay()
     *
     * Uppdaterar fälten för korten samt textdisplayen
     */
    private void updateDisplay()
    {
        // Uppdatera dealer panelen
        dealerCardsPanel.removeAll();
        for (int i = 0; i < gameController.getAmountDealerCards(); i++)
        {
            Card card = gameController.getDealerCard(i);
            dealerCardsPanel.add(new CardPanel(card));
        }

        // Uppdatera spelarens panel
        playerCardsPanel.removeAll();
        for (Card card : gameController.getPlayerHand())
        {
            playerCardsPanel.add(new CardPanel(card));
        }

        // Uppdatera poängen
        int dealerValue = gameController.calculateDealerHandsValue();
        int playerValue = gameController.calculatePlayerHandsValue();

        dealerLabel.setText("Dealer: " + dealerValue );
        playerLabel.setText("Spelare: " + playerValue);

        // Måla om UI:n
        dealerCardsPanel.revalidate();
        dealerCardsPanel.repaint();
        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();
    }

    /**
     * determineWinner()
     *
     * Räkna ut vem som vann spelet
     *
     */
    private void determineWinner()
    {
        int playerValue = gameController.calculatePlayerHandsValue();
        int dealerValue = gameController.calculateDealerHandsValue();

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
        gameController.initializeDeck();

        // Rensa panelen
        dealerCardsPanel.removeAll();
        playerCardsPanel.removeAll();

        gameController.addPlayerCard();
        gameController.addDealerCard();
        gameController.addPlayerCard();

        updateDisplay();

        // Stäng av eller sätt igång knapparna
        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        newGameButton.setEnabled(false);

        // Kolla efter blackjack
        if (gameController.calculatePlayerHandsValue() == 21)
        {
            playerStands();
        }
    }

    /**
     * playerHits()
     *
     * Spelaren trycker på ta nytt kort knappen
     */
    private void playerHits()
    {
        if (!gameController.isGameOver())
        {
            gameController.addPlayerCard();
            updateDisplay();

            int playerValue = gameController.calculatePlayerHandsValue();
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
        if (!gameController.isGameOver())
        {
            gameController.endGame();
            endGame(false);

            // Dealer draws cards
            while (gameController.calculateDealerHandsValue() < 17)
                gameController.addDealerCard();

            updateDisplay();
            determineWinner();
        }
    }
}
