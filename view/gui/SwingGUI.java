package view.gui;

import controller.GameController;
import model.*;
import observer.GameObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Observer Pattern - Observer
 * 
 * Swing-based graphical user interface for the UNO game.
 * Automatically updates when the game state changes.
 */
public class SwingGUI extends JFrame implements GameObserver {
    private GameController controller;
    private JPanel mainPanel;
    private JPanel centerPanel;
    private JPanel handPanel;
    private JLabel topCardLabel;
    private JLabel currentPlayerLabel;
    private JLabel statusLabel;
    private JButton drawButton;

    public SwingGUI(GameController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("🎴 UNO Game 🎴");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Main panel with gradient background
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(26, 26, 46));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel - Game info
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel - Game board
        centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel - Player hand
        handPanel = createHandPanel();
        mainPanel.add(handPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(40, 40, 70));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("🎴 UNO GAME 🎴");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        currentPlayerLabel = new JLabel("Current Player: ");
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        currentPlayerLabel.setForeground(Color.YELLOW);
        currentPlayerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusLabel = new JLabel("Game Status");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setForeground(Color.LIGHT_GRAY);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(currentPlayerLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(statusLabel);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(26, 26, 46));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        topCardLabel = new JLabel("Top Card: ");
        topCardLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topCardLabel.setForeground(Color.WHITE);
        topCardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topCardLabel.setOpaque(true);
        topCardLabel.setBackground(new Color(60, 60, 90));
        topCardLabel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        drawButton = new JButton("🎴 Draw Card");
        drawButton.setFont(new Font("Arial", Font.BOLD, 18));
        drawButton.setBackground(new Color(76, 175, 80));
        drawButton.setForeground(Color.WHITE);
        drawButton.setFocusPainted(false);
        drawButton.setBorderPainted(false);
        drawButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        drawButton.addActionListener(e -> handleDrawCard());

        panel.add(Box.createVerticalGlue());
        panel.add(topCardLabel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(drawButton);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createHandPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(20, 20, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return panel;
    }

    @Override
    public void update(GameState state) {
        SwingUtilities.invokeLater(() -> {
            // Check for winner
            Player winner = controller.getWinner();
            if (winner != null) {
                showWinnerDialog(winner);
                return;
            }

            // Update display
            updateGameDisplay(state);

            // Handle AI turns
            Player currentPlayer = state.getCurrentPlayer();
            if (!currentPlayer.isHuman() && controller.isGameRunning()) {
                Timer timer = new Timer(1500, e -> controller.makeAIMove());
                timer.setRepeats(false);
                timer.start();
            }
        });
    }

    private void updateGameDisplay(GameState state) {
        // Update current player
        Player currentPlayer = state.getCurrentPlayer();
        currentPlayerLabel.setText("Current Player: " + currentPlayer.getName());

        // Update top card
        Card topCard = state.getTopCard();
        topCardLabel.setText("  Top Card: " + topCard.toString() + "  ");
        topCardLabel.setBackground(getCardColor(topCard.getColor()));

        // Update status
        statusLabel.setText("Direction: " + state.getDirectionSymbol() +
                " | Draw Pile: " + controller.getDeck().getDrawPileSize() + " cards");

        // Update player hand
        if (currentPlayer.isHuman()) {
            updatePlayerHand(currentPlayer, topCard);
        } else {
            handPanel.removeAll();
            JLabel aiLabel = new JLabel("AI Player's Turn - Please wait...");
            aiLabel.setFont(new Font("Arial", Font.BOLD, 18));
            aiLabel.setForeground(Color.ORANGE);
            handPanel.add(aiLabel);
        }

        handPanel.revalidate();
        handPanel.repaint();
    }

    private void updatePlayerHand(Player player, Card topCard) {
        handPanel.removeAll();

        JLabel handLabel = new JLabel("Your Hand: ");
        handLabel.setFont(new Font("Arial", Font.BOLD, 16));
        handLabel.setForeground(Color.WHITE);
        handPanel.add(handLabel);

        List<Card> hand = player.getHand();
        for (int i = 0; i < hand.size(); i++) {
            final int index = i;
            Card card = hand.get(i);
            JButton cardButton = createCardButton(card, index, topCard);
            handPanel.add(cardButton);
        }
    }

    private JButton createCardButton(Card card, int index, Card topCard) {
        JButton button = new JButton(card.toString());
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(getCardColor(card.getColor()));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 60));

        boolean canPlay = card.canPlayOn(topCard);
        button.setEnabled(canPlay);

        if (!canPlay) {
            button.setBackground(Color.GRAY);
        }

        if (canPlay) {
            button.addActionListener(e -> handleCardPlay(index));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        return button;
    }

    private Color getCardColor(CardColor color) {
        switch (color) {
            case RED:
                return new Color(231, 76, 60);
            case BLUE:
                return new Color(52, 152, 219);
            case GREEN:
                return new Color(46, 204, 113);
            case YELLOW:
                return new Color(243, 156, 18);
            case WILD:
                return new Color(155, 89, 182);
            default:
                return Color.GRAY;
        }
    }

    private void handleCardPlay(int index) {
        boolean success = controller.playTurn(index);

        if (success && controller.getGameState().isColorChangeNeeded()) {
            showColorPicker();
        }
    }

    private void handleDrawCard() {
        controller.playTurn(-1);
    }

    private void showColorPicker() {
        Object[] options = { "Red", "Blue", "Green", "Yellow" };
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose a color:",
                "Wild Card",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice >= 0) {
            CardColor[] colors = { CardColor.RED, CardColor.BLUE, CardColor.GREEN, CardColor.YELLOW };
            controller.selectColor(colors[choice]);
        }
    }

    private void showWinnerDialog(Player winner) {
        JOptionPane.showMessageDialog(
                this,
                "🎉 " + winner.getName() + " WINS! 🎉\n\nCongratulations!",
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    public void start() {
        update(controller.getGameState());
    }
}
