package model;

import model.strategy.CardEffect;

/**
 * Composite Pattern - Leaf
 * Strategy Pattern - Context
 * 
 * Represents a wild card (Wild or Wild Draw Four) in UNO.
 * Uses the Strategy pattern to delegate effect execution.
 */
public class WildCard extends Card {
    private final CardEffect effect;

    /**
     * Constructor for WildCard.
     * 
     * @param type   The type of wild card
     * @param effect The strategy for this card's effect
     */
    public WildCard(CardType type, CardEffect effect) {
        super(CardColor.WILD, type, -1);
        this.effect = effect;

        // Validate that this is actually a wild card type
        if (type != CardType.WILD && type != CardType.WILD_DRAW_FOUR) {
            throw new IllegalArgumentException("WildCard must be WILD or WILD_DRAW_FOUR");
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
     * A wild card can always be played.
     * 
     * @param topCard The card on top of the discard pile
     * @return always true
     */
    @Override
    public boolean canPlayOn(Card topCard) {
        return true; // Wild cards can always be played
    }

    public CardEffect getEffect() {
        return effect;
    }
}
