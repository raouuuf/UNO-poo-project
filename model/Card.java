package model;

/**
 * Composite Pattern - Component (Abstract Base Class)
 * 
 * Abstract base class for all UNO cards.
 * Defines common properties and behaviors for all card types.
 */
public abstract class Card {
    protected CardColor color;
    protected CardType type;
    protected int value; // For number cards, -1 for action/wild cards

    /**
     * Constructor for Card.
     * 
     * @param color The color of the card
     * @param type  The type of the card
     * @param value The numeric value (0-9 for number cards, -1 for others)
     */
    public Card(CardColor color, CardType type, int value) {
        this.color = color;
        this.type = type;
        this.value = value;
    }

    /**
     * Executes the card's effect when played.
     * 
     * @param state The current game state
     */
    public abstract void execute(GameState state);

    /**
     * Checks if this card can be played on top of another card.
     * 
     * @param topCard The card on top of the discard pile
     * @return true if this card can be played
     */
    public abstract boolean canPlayOn(Card topCard);

    // Getters
    public CardColor getColor() {
        return color;
    }

    public CardType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    /**
     * Sets the color of the card (used for wild cards).
     * 
     * @param color The new color
     */
    public void setColor(CardColor color) {
        this.color = color;
    }

    @Override
    public String toString() {
        if (type == CardType.NUMBER) {
            return color + " " + value;
        } else if (type == CardType.WILD || type == CardType.WILD_DRAW_FOUR) {
            return type.getDisplayName();
        } else {
            return color + " " + type.getDisplayName();
        }
    }
}
