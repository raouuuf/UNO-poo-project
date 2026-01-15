package controller;

import model.*;
import view.ConsoleView;

/**
 * Singleton Pattern - Manages the overall game lifecycle.
 * 
 * High-level game management and initialization.
 */
public class GameManager {
    private static GameManager instance;
    private GameState gameState;
    private GameController gameController;
    private Deck deck;
    private boolean isGUIMode;

    /**
     * Private constructor for Singleton pattern.
     */
    private GameManager() {
        this.gameState = new GameState();
        this.deck = Deck.getInstance();
        this.isGUIMode = false;
    }

    /**
     * Gets the singleton instance of GameManager.
     * 
     * @return The single GameManager instance
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Initializes a new game with the specified number of players.
     * 
     * @param playerCount      Number of players (2-10)
     * @param humanPlayerCount Number of human players
     * @param useGUI           Whether to use GUI mode
     */
    public void initializeGame(int playerCount, int humanPlayerCount, boolean useGUI) {
        if (playerCount < 2 || playerCount > 10) {
            throw new IllegalArgumentException("Player count must be between 2 and 10");
        }

        if (humanPlayerCount < 1 || humanPlayerCount > playerCount) {
            throw new IllegalArgumentException("Invalid human player count");
        }

        this.isGUIMode = useGUI;

        // Reset the deck
        deck.reset();

        // Create players
        java.util.List<Player> players = new java.util.ArrayList<>();
        for (int i = 0; i < humanPlayerCount; i++) {
            players.add(new Player("Player " + (i + 1), true));
        }
        for (int i = humanPlayerCount; i < playerCount; i++) {
            players.add(new Player("AI " + (i - humanPlayerCount + 1), false));
        }

        gameState.setPlayers(players);

        // Deal initial hands (7 cards each)
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                player.drawCard(deck.draw());
            }
        }

        // Set initial top card (make sure it's not a wild card)
        Card initialCard;
        do {
            initialCard = deck.draw();
        } while (initialCard.getType() == CardType.WILD ||
                initialCard.getType() == CardType.WILD_DRAW_FOUR);

        deck.addToDiscard(initialCard);
        gameState.setTopCard(initialCard);

        // Create controller
        gameController = new GameController(gameState, deck);

        // Create and attach view(s)
        if (useGUI) {
            // Use Swing GUI (built into Java, no downloads needed!)
            try {
                view.gui.SwingGUI gui = new view.gui.SwingGUI(gameController);
                gameState.addObserver(gui);
                System.out.println("Swing GUI initialized successfully!");
            } catch (Exception e) {
                System.err.println("Warning: Could not initialize GUI: " + e.getMessage());
                System.err.println("Using console mode instead.");
                this.isGUIMode = false;
                ConsoleView consoleView = new ConsoleView(gameController);
                gameState.addObserver(consoleView);
            }
        } else {
            ConsoleView consoleView = new ConsoleView(gameController);
            gameState.addObserver(consoleView);
        }
    }

    /**
     * Starts the game.
     */
    public void startGame() {
        if (gameController == null) {
            throw new IllegalStateException("Game not initialized. Call initializeGame() first.");
        }

        gameController.startGame();
    }

    /**
     * Gets the game state.
     * 
     * @return The current game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Gets the game controller.
     * 
     * @return The game controller
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Checks if GUI mode is enabled.
     * 
     * @return true if using GUI
     */
    public boolean isGUIMode() {
        return isGUIMode;
    }
}
