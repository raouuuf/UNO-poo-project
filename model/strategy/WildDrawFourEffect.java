package model.strategy;

import model.GameState;

/**
 * Strategy Pattern - Concrete Strategy
 * 
 * Implements the Wild Draw Four card effect.
 * Forces the next player to draw 4 cards, skip their turn,
 * and allows the current player to choose a new color.
 */
public class WildDrawFourEffect implements CardEffect {

    @Override
    public void applyEffect(GameState state) {
        state.setPendingDrawCount(4);
        state.setColorChangeNeeded(true);
    }

    @Override
    public String getDescription() {
        return "Next player draws 4 cards, skips turn, and you choose a color";
    }
}
