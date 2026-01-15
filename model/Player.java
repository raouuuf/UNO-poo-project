package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the UNO game.
 * Manages the player's hand and provides methods for card operations.
 */
public class Player {
    private final String name;
    private final List<Card> hand;
    private final boolean isHuman;

    /**
     * Constructor for Player.
     * 
     * @param name    The player's name
     * @param isHuman Whether this is a human or AI player
     */
    public Player(String name, boolean isHuman) {
        this.name = name;
        this.isHuman = isHuman;
        this.hand = new ArrayList<>();
    }

    /**
     * Adds a card to the player's hand.
     * 
     * @param card The card to add
     */
    public void drawCard(Card card) {
        hand.add(card);
    }

    /**
     * Removes and returns a card from the player's hand.
     * 
     * @param index The index of the card to play
     * @return The card that was played
     */
    public Card playCard(int index) {
        if (index < 0 || index >= hand.size()) {
            throw new IndexOutOfBoundsException("Invalid card index: " + index);
        }
        return hand.remove(index);
    }

    /**
     * Checks if the player has any valid cards to play.
     * 
     * @param topCard The card on top of the discard pile
     * @return true if the player has at least one valid card
     */
    public boolean hasValidCard(Card topCard) {
        for (Card card : hand) {
            if (card.canPlayOn(topCard)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets all valid cards that can be played on the top card.
     * 
     * @param topCard The card on top of the discard pile
     * @return List of valid card indices
     */
    public List<Integer> getValidCardIndices(Card topCard) {
        List<Integer> validIndices = new ArrayList<>();
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).canPlayOn(topCard)) {
                validIndices.add(i);
            }
        }
        return validIndices;
    }

    /**
     * Checks if the player has won (no cards left).
     * 
     * @return true if hand is empty
     */
    public boolean hasWon() {
        return hand.isEmpty();
    }

    // Getters
    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return new ArrayList<>(hand); // Return a copy to prevent external modification
    }

    public int getHandSize() {
        return hand.size();
    }

    public boolean isHuman() {
        return isHuman;
    }

    @Override
    public String toString() {
        return name + " (" + hand.size() + " cards)";
    }
}
