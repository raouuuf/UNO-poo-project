package model.strategy;

import model.GameState;

/**
 * Strategy Pattern - Concrete Strategy
 * 
 * Implements the Draw Two card effect.
 * Forces the next player to draw 2 cards and skip their turn.
 */
public class DrawTwoEffect implements CardEffect {

    @Override
    public void applyEffect(GameState state) {
        state.setPendingDrawCount(2);
    }

    @Override
    public String getDescription() {
        return "Next player draws 2 cards and skips turn";
    }
}
