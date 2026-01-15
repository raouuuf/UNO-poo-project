package controller;

import model.*;
import java.util.List;
import java.util.Random;

/**
 * Main game controller - coordinates game flow and logic.
 * Handles turn management, card plays, and game rules.
 */
public class GameController {
    private final GameState gameState;
    private final Deck deck;
    private boolean gameRunning;
    private Player winner;
    private Random random;

    /**
     * Constructor for GameController.
     * 
     * @param gameState The game state to manage
     * @param deck      The deck to use
     */
    public GameController(GameState gameState, Deck deck) {
        this.gameState = gameState;
        this.deck = deck;
        this.gameRunning = false;
        this.random = new Random();
    }

    /**
     * Starts the game loop.
     */
    public void startGame() {
        gameRunning = true;
        gameState.notifyObservers(); // Initial update
    }

    /**
     * Processes a player's turn.
     * 
     * @param cardIndex The index of the card to play, or -1 to draw
     * @return true if the turn was successful
     */
    public boolean playTurn(int cardIndex) {
        if (!gameRunning) {
            return false;
        }

        Player currentPlayer = gameState.getCurrentPlayer();

        // Handle pending draw (from Draw Two or Wild Draw Four)
        if (gameState.getPendingDrawCount() > 0) {
            for (int i = 0; i < gameState.getPendingDrawCount(); i++) {
                currentPlayer.drawCard(deck.draw());
            }
            gameState.setPendingDrawCount(0);
            gameState.advanceTurn();
            return true;
        }

        // Player chooses to draw a card
        if (cardIndex == -1) {
            Card drawnCard = deck.draw();
            currentPlayer.drawCard(drawnCard);

            // Check if the drawn card can be played immediately
            if (drawnCard.canPlayOn(gameState.getTopCard())) {
                // Player can choose to play it or keep it
                // For now, we'll just advance the turn
                gameState.advanceTurn();
                return true;
            }

            gameState.advanceTurn();
            return true;
        }

        // Player chooses to play a card
        List<Card> hand = currentPlayer.getHand();
        if (cardIndex < 0 || cardIndex >= hand.size()) {
            return false; // Invalid card index
        }

        Card cardToPlay = hand.get(cardIndex);

        // Check if the card can be played
        if (!cardToPlay.canPlayOn(gameState.getTopCard())) {
            return false; // Invalid play
        }

        // Play the card
        Card playedCard = currentPlayer.playCard(cardIndex);
        deck.addToDiscard(playedCard);
        gameState.setTopCard(playedCard);

        // Execute card effect
        playedCard.execute(gameState);

        // Check for winner
        if (currentPlayer.hasWon()) {
            winner = currentPlayer;
            gameRunning = false;
            gameState.notifyObservers();
            return true;
        }

        // Advance turn if no special effect prevents it
        if (gameState.getPendingDrawCount() == 0 &&
                playedCard.getType() != CardType.SKIP) {
            gameState.advanceTurn();
        } else if (playedCard.getType() == CardType.SKIP) {
            // Skip effect already advanced the turn once in the strategy
            // We don't need to advance again
        }

        return true;
    }

    /**
     * Handles color selection for wild cards.
     * 
     * @param color The chosen color
     */
    public void selectColor(CardColor color) {
        if (color == CardColor.WILD) {
            throw new IllegalArgumentException("Cannot select WILD as a color");
        }

        Card topCard = gameState.getTopCard();
        topCard.setColor(color);
        gameState.setColorChangeNeeded(false);
        gameState.notifyObservers();
    }

    /**
     * AI player makes a move.
     * 
     * @return The index of the card played, or -1 if drew a card
     */
    public int makeAIMove() {
        Player currentPlayer = gameState.getCurrentPlayer();
        Card topCard = gameState.getTopCard();

        // Handle pending draw
        if (gameState.getPendingDrawCount() > 0) {
            playTurn(-1); // Just draw the cards
            return -1;
        }

        // Find valid cards
        List<Integer> validIndices = currentPlayer.getValidCardIndices(topCard);

        if (validIndices.isEmpty()) {
            // No valid cards, must draw
            playTurn(-1);
            return -1;
        }

        // Play a random valid card
        int chosenIndex = validIndices.get(random.nextInt(validIndices.size()));
        playTurn(chosenIndex);

        // If it was a wild card, choose a random color
        if (gameState.isColorChangeNeeded()) {
            CardColor[] colors = { CardColor.RED, CardColor.BLUE, CardColor.GREEN, CardColor.YELLOW };
            selectColor(colors[random.nextInt(colors.length)]);
        }

        return chosenIndex;
    }

    /**
     * Draws a card for the current player.
     */
    public void drawCard() {
        Player currentPlayer = gameState.getCurrentPlayer();
        currentPlayer.drawCard(deck.draw());
        gameState.notifyObservers();
    }

    /**
     * Checks if the game is still running.
     * 
     * @return true if game is running
     */
    public boolean isGameRunning() {
        return gameRunning;
    }

    /**
     * Gets the winner of the game.
     * 
     * @return The winning player, or null if no winner yet
     */
    public Player getWinner() {
        return winner;
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
     * Gets the deck.
     * 
     * @return The deck
     */
    public Deck getDeck() {
        return deck;
    }
}
