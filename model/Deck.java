package model;

import model.strategy.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton Pattern - Manages the deck of UNO cards.
 * 
 * Ensures only one deck exists per game.
 * Manages both the draw pile and discard pile.
 */
public class Deck {
    private static Deck instance;
    private List<Card> drawPile;
    private List<Card> discardPile;

    /**
     * Private constructor for Singleton pattern.
     * Initializes a standard UNO deck (108 cards).
     */
    private Deck() {
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();
        initializeDeck();
    }

    /**
     * Gets the singleton instance of the Deck.
     * 
     * @return The single Deck instance
     */
    public static Deck getInstance() {
        if (instance == null) {
            instance = new Deck();
        }
        return instance;
    }

    /**
     * Resets the deck to a new game state.
     */
    public void reset() {
        drawPile.clear();
        discardPile.clear();
        initializeDeck();
    }

    /**
     * Initializes a standard UNO deck with 108 cards:
     * - Number cards (0-9) in 4 colors: 1 zero per color, 2 of each 1-9
     * - Action cards (Skip, Reverse, Draw Two) in 4 colors: 2 of each per color
     * - Wild cards: 4 Wild, 4 Wild Draw Four
     */
    private void initializeDeck() {
        CardColor[] colors = { CardColor.RED, CardColor.BLUE, CardColor.GREEN, CardColor.YELLOW };

        // Add number cards
        for (CardColor color : colors) {
            // One 0 per color
            drawPile.add(new NumberCard(color, 0));

            // Two of each 1-9 per color
            for (int i = 1; i <= 9; i++) {
                drawPile.add(new NumberCard(color, i));
                drawPile.add(new NumberCard(color, i));
            }
        }

        // Add action cards (2 of each per color)
        for (CardColor color : colors) {
            drawPile.add(new ActionCard(color, CardType.SKIP, new SkipEffect()));
            drawPile.add(new ActionCard(color, CardType.SKIP, new SkipEffect()));

            drawPile.add(new ActionCard(color, CardType.REVERSE, new ReverseEffect()));
            drawPile.add(new ActionCard(color, CardType.REVERSE, new ReverseEffect()));

            drawPile.add(new ActionCard(color, CardType.DRAW_TWO, new DrawTwoEffect()));
            drawPile.add(new ActionCard(color, CardType.DRAW_TWO, new DrawTwoEffect()));
        }

        // Add wild cards (4 of each)
        for (int i = 0; i < 4; i++) {
            drawPile.add(new WildCard(CardType.WILD, new WildEffect()));
            drawPile.add(new WildCard(CardType.WILD_DRAW_FOUR, new WildDrawFourEffect()));
        }

        shuffle();
    }

    /**
     * Shuffles the draw pile.
     */
    public void shuffle() {
        Collections.shuffle(drawPile);
    }

    /**
     * Draws a card from the draw pile.
     * If the draw pile is empty, reshuffles the discard pile.
     * 
     * @return The drawn card
     */
    public Card draw() {
        if (drawPile.isEmpty()) {
            reshuffleDiscardPile();
        }

        if (drawPile.isEmpty()) {
            throw new IllegalStateException("No cards left to draw!");
        }

        return drawPile.remove(drawPile.size() - 1);
    }

    /**
     * Adds a card to the discard pile.
     * 
     * @param card The card to discard
     */
    public void addToDiscard(Card card) {
        discardPile.add(card);
    }

    /**
     * Gets the top card of the discard pile without removing it.
     * 
     * @return The top card of the discard pile
     */
    public Card getTopCard() {
        if (discardPile.isEmpty()) {
            return null;
        }
        return discardPile.get(discardPile.size() - 1);
    }

    /**
     * Reshuffles the discard pile into the draw pile.
     * Keeps the top card of the discard pile.
     */
    private void reshuffleDiscardPile() {
        if (discardPile.size() <= 1) {
            return; // Keep at least the top card
        }

        // Keep the top card
        Card topCard = discardPile.remove(discardPile.size() - 1);

        // Move all other cards to draw pile
        drawPile.addAll(discardPile);
        discardPile.clear();
        discardPile.add(topCard);

        // Shuffle the new draw pile
        shuffle();
    }

    /**
     * Gets the number of cards remaining in the draw pile.
     * 
     * @return Number of cards in draw pile
     */
    public int getDrawPileSize() {
        return drawPile.size();
    }

    /**
     * Gets the number of cards in the discard pile.
     * 
     * @return Number of cards in discard pile
     */
    public int getDiscardPileSize() {
        return discardPile.size();
    }
}
