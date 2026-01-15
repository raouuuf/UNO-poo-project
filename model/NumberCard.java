package model;

/**
 * Composite Pattern - Leaf
 * 
 * Represents a number card (0-9) in UNO.
 * Number cards have no special effects.
 */
public class NumberCard extends Card {

    /**
     * Constructor for NumberCard.
     * 
     * @param color The color of the card
     * @param value The number on the card (0-9)
     */
    public NumberCard(CardColor color, int value) {
        super(color, CardType.NUMBER, value);
        if (value < 0 || value > 9) {
            throw new IllegalArgumentException("Number card value must be between 0 and 9");
        }
    }

    /**
     * Number cards have no special effect.
     * 
     * @param state The current game state
     */
    @Override
    public void execute(GameState state) {
        // Number cards have no special effect
        // Just advance to the next player (handled by GameController)
    }

    /**
     * A number card can be played if it matches the color or value of the top card.
     * 
     * @param topCard The card on top of the discard pile
     * @return true if colors or values match
     */
    @Override
    public boolean canPlayOn(Card topCard) {
        return this.color == topCard.getColor() ||
                (topCard.getType() == CardType.NUMBER && this.value == topCard.getValue());
    }
}
