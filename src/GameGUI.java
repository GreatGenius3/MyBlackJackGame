import javax.swing.*;
import java.awt.*;
import java.util.Collections;


public class GameGUI extends JFrame {
    private JButton hitButton;
    private JButton standButton;
    private JButton newGameButton;
    private JLabel dealerLabel;
    private JLabel playerLabel;
    private JLabel statusLabel;
    private JPanel dealerCardsPanel;
    private JPanel playerCardsPanel;

    GameController gameController;


    public GameGUI() {
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

    private void initializeComponents() {

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

        hitButton.setEnabled(false);
        standButton.setEnabled(false);
    }

    private void setupLayout() {
        JPanel dealerPanel = new JPanel();
        dealerPanel.add(dealerLabel, BorderLayout.NORTH);
        dealerPanel.add(dealerCardsPanel, BorderLayout.CENTER);

        dealerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        dealerPanel.setBorder(BorderFactory.createTitledBorder("Dealer"));
        dealerPanel.setBackground(new Color(184, 219, 128));
        dealerPanel.setPreferredSize(new Dimension(600, 400));

        JPanel playerPanel = new JPanel();
        playerPanel.add(playerLabel, BorderLayout.NORTH);
        playerPanel.add(playerCardsPanel, BorderLayout.CENTER);

        playerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        playerPanel.setBorder(BorderFactory.createTitledBorder("Player"));
        playerPanel.setBackground(new Color(184, 219, 128));
        playerPanel.setPreferredSize(new Dimension(600, 300));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(newGameButton);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(statusLabel, BorderLayout.CENTER);

        add(dealerPanel, BorderLayout.NORTH);
        add(playerPanel, BorderLayout.CENTER);

        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.add(buttonPanel, BorderLayout.CENTER);
        bottomContainer.add(statusPanel, BorderLayout.SOUTH);

        add(bottomContainer, BorderLayout.SOUTH);

        hitButton.setVisible(true);
        standButton.setVisible(true);
        newGameButton.setVisible(true);
    }

    private void setupEventHandlers() {
        hitButton.addActionListener(e -> playerHits());
        standButton.addActionListener(e -> playerStands());
        newGameButton.addActionListener(e -> startNewGame());
    }

    private void updateDisplay() {
        dealerCardsPanel.removeAll();
        for (int i = 0; i < gameController.getAmountDealerCards(); i++) {
            Card card = gameController.getDealerCard(i);
            dealerCardsPanel.add(CardFactory.create(card));
        }
        dealerCardsPanel.add(CardFactory.create());

        playerCardsPanel.removeAll();
        for (Card card : gameController.getPlayerHand()) {
            playerCardsPanel.add(CardFactory.create(card));
        }


        int dealerValue = gameController.calculateDealerHandsValue();
        int playerValue = gameController.calculatePlayerHandsValue();

        dealerLabel.setText("Dealer: " + dealerValue);
        playerLabel.setText("Spelare: " + playerValue);

        dealerCardsPanel.revalidate();
        dealerCardsPanel.repaint();
        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();
    }

    private void determineWinner() {
        int playerValue = gameController.calculatePlayerHandsValue();
        int dealerValue = gameController.calculateDealerHandsValue();

        if (playerValue > 21) {
            statusLabel.setText("Du förlorade! Du fick " + playerValue);
        } else if (dealerValue > 21) {
            statusLabel.setText("Grattis! Du vann! Banken fick " + dealerValue);
        } else if (playerValue > dealerValue) {
            statusLabel.setText("Grattis! Du vann! " + playerValue + " mot " + dealerValue);
        } else if (playerValue < dealerValue) {
            statusLabel.setText("Du förlorade! " + playerValue + " mot " + dealerValue);
        } else {
            statusLabel.setText("Oavgjort! " + playerValue + " lika med " + dealerValue);
        }
    }


    private void endGame(boolean playerWon) {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        newGameButton.setEnabled(true);
    }

    private void startNewGame() {
        gameController.initializeDeck();

        dealerCardsPanel.removeAll();
        playerCardsPanel.removeAll();

        gameController.addPlayerCard();
        gameController.addDealerCard();
        gameController.addPlayerCard();

        updateDisplay();

        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        newGameButton.setEnabled(false);

        if (gameController.calculatePlayerHandsValue() == 21) {
            playerStands();
        }
    }

    private void playerHits() {
        if (!gameController.isGameOver()) {
            gameController.addPlayerCard();
            updateDisplay();

            int playerValue = gameController.calculatePlayerHandsValue();
            if (playerValue > 21) {
                statusLabel.setText("Du förlorade! Du fick " + playerValue);
                endGame(false);
            }
        }
    }

    private void playerStands() {
        if (!gameController.isGameOver()) {
            gameController.endGame();
            endGame(false);

            while (gameController.calculateDealerHandsValue() < 17) gameController.addDealerCard();

            updateDisplay();
            determineWinner();
        }
    }
}
