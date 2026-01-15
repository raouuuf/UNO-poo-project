package model;

import model.strategy.CardEffect;

/**
 * Composite Pattern - Leaf
 * Strategy Pattern - Context
 * 
 * Represents an action card (Skip, Reverse, Draw Two) in UNO.
 * Uses the Strategy pattern to delegate effect execution.
 */
public class ActionCard extends Card {
    private final CardEffect effect;

    /**
     * Constructor for ActionCard.
     * 
     * @param color  The color of the card
     * @param type   The type of action card
     * @param effect The strategy for this card's effect
     */
    public ActionCard(CardColor color, CardType type, CardEffect effect) {
        super(color, type, -1);
        this.effect = effect;

        // Validate that this is actually an action card type
        if (type != CardType.SKIP && type != CardType.REVERSE && type != CardType.DRAW_TWO) {
            throw new IllegalArgumentException("ActionCard must be SKIP, REVERSE, or DRAW_TWO");
        }
    }

    /**
     * Executes the card's effect using the Strategy pattern.
     * 
     * @param state The current game state
     */
    @Override
    public void execute(GameState state) {
        effect.applyEffect(state);
    }

    /**
     * An action card can be played if it matches the color or type of the top card.
     * 
     * @param topCard The card on top of the discard pile
     * @return true if colors or types match
     */
    @Override
    public boolean canPlayOn(Card topCard) {
        return this.color == topCard.getColor() || this.type == topCard.getType();
    }

    public CardEffect getEffect() {
        return effect;
    }
}
