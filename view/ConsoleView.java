package view;

import controller.GameController;
import model.*;
import observer.GameObserver;
import java.util.List;

/**
 * Observer Pattern - Observer
 * 
 * Console-based view for the UNO game.
 * Automatically updates when the game state changes.
 */
public class ConsoleView implements GameObserver {
    private final GameController controller;
    private final InputHandler inputHandler;

    /**
     * Constructor for ConsoleView.
     * 
     * @param controller The game controller
     */
    public ConsoleView(GameController controller) {
        this.controller = controller;
        this.inputHandler = new InputHandler();
    }

    /**
     * Observer pattern update method.
     * Called when the game state changes.
     * 
     * @param state The updated game state
     */
    @Override
    public void update(GameState state) {
        clearScreen();
        displayGameState(state);

        // Check for winner
        Player winner = controller.getWinner();
        if (winner != null) {
            displayWinner(winner);
            return;
        }

        // Handle current player's turn
        Player currentPlayer = state.getCurrentPlayer();

        if (currentPlayer.isHuman()) {
            handleHumanTurn(currentPlayer, state);
        } else {
            handleAITurn(currentPlayer, state);
        }
    }

    /**
     * Displays the current game state.
     * 
     * @param state The game state
     */
    private void displayGameState(GameState state) {
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("                        UNO GAME                           ");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println();

        // Display top card
        Card topCard = state.getTopCard();
        System.out.println("Top Card: " + getColoredCard(topCard));
        System.out.println("Direction: " + state.getDirectionSymbol());
        System.out.println();

        // Display all players
        System.out.println("Players:");
        List<Player> players = state.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            String marker = (i == state.getCurrentPlayerIndex()) ? ">>> " : "    ";
            System.out.println(marker + player.getName() + ": " + player.getHandSize() + " cards");
        }
        System.out.println();

        // Display deck info
        System.out.println("Draw Pile: " + controller.getDeck().getDrawPileSize() + " cards");
        System.out.println();
    }

    /**
     * Displays a player's hand.
     * 
     * @param player  The player
     * @param topCard The top card to check validity against
     */
    private void displayHand(Player player, Card topCard) {
        System.out.println(player.getName() + "'s Hand:");
        List<Card> hand = player.getHand();

        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            boolean canPlay = card.canPlayOn(topCard);
            String status = canPlay ? "[✓]" : "[ ]";
            System.out.println("  " + (i + 1) + ". " + status + " " + getColoredCard(card));
        }
        System.out.println();
    }

    /**
     * Handles a human player's turn.
     * 
     * @param player The current player
     * @param state  The game state
     */
    private void handleHumanTurn(Player player, GameState state) {
        System.out.println("───────────────────────────────────────────────────────────");
        System.out.println(player.getName() + "'s Turn");
        System.out.println("───────────────────────────────────────────────────────────");

        // Check for pending draw
        if (state.getPendingDrawCount() > 0) {
            System.out.println("You must draw " + state.getPendingDrawCount() + " cards!");
            inputHandler.waitForEnter();
            controller.playTurn(-1);
            return;
        }

        displayHand(player, state.getTopCard());

        // Get player's choice
        int choice = inputHandler.getCardChoice(player.getHandSize());

        // Attempt to play the card
        boolean success = controller.playTurn(choice);

        if (!success && choice != -1) {
            System.out.println("Invalid play! That card cannot be played.");
            inputHandler.waitForEnter();
            return;
        }

        // Handle color selection for wild cards
        if (state.isColorChangeNeeded()) {
            CardColor chosenColor = inputHandler.getColorChoice();
            controller.selectColor(chosenColor);
            System.out.println("Color changed to " + chosenColor);
        }
    }

    /**
     * Handles an AI player's turn.
     * 
     * @param player The current player
     * @param state  The game state
     */
    private void handleAITurn(Player player, GameState state) {
        System.out.println("───────────────────────────────────────────────────────────");
        System.out.println(player.getName() + "'s Turn (AI)");
        System.out.println("───────────────────────────────────────────────────────────");

        // Check for pending draw
        if (state.getPendingDrawCount() > 0) {
            System.out.println(player.getName() + " draws " + state.getPendingDrawCount() + " cards!");
        } else {
            int cardIndex = controller.makeAIMove();

            if (cardIndex == -1) {
                System.out.println(player.getName() + " draws a card.");
            } else {
                System.out.println(player.getName() + " plays a card.");
            }
        }

        System.out.println();
        inputHandler.waitForEnter();
    }

    /**
     * Displays the winner.
     * 
     * @param winner The winning player
     */
    private void displayWinner(Player winner) {
        System.out.println();
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("                    🎉 GAME OVER! 🎉                       ");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("           " + winner.getName() + " WINS!");
        System.out.println();
        System.out.println("═══════════════════════════════════════════════════════════");
    }

    /**
     * Gets a colored representation of a card for console display.
     * 
     * @param card The card
     * @return Colored string representation
     */
    private String getColoredCard(Card card) {
        // ANSI color codes
        String colorCode = "";
        String reset = "\u001B[0m";

        switch (card.getColor()) {
            case RED:
                colorCode = "\u001B[31m"; // Red
                break;
            case BLUE:
                colorCode = "\u001B[34m"; // Blue
                break;
            case GREEN:
                colorCode = "\u001B[32m"; // Green
                break;
            case YELLOW:
                colorCode = "\u001B[33m"; // Yellow
                break;
            case WILD:
                colorCode = "\u001B[35m"; // Magenta
                break;
        }

        return colorCode + card.toString() + reset;
    }

    /**
     * Clears the console screen.
     */
    private void clearScreen() {
        // Print multiple newlines to simulate clearing
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    /**
     * Closes the input handler.
     */
    public void close() {
        inputHandler.close();
    }
}
