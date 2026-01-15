package model.strategy;

import model.GameState;

/**
 * Strategy Pattern - Concrete Strategy
 * 
 * Implements the Skip card effect.
 * Skips the next player's turn.
 */
public class SkipEffect implements CardEffect {

    @Override
    public void applyEffect(GameState state) {
        // Skip the next player by advancing the turn twice
        state.advanceTurn();
    }

    @Override
    public String getDescription() {
        return "Skip next player's turn";
    }
}
