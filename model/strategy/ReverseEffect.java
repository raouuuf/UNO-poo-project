package model.strategy;

import model.GameState;

/**
 * Strategy Pattern - Concrete Strategy
 * 
 * Implements the Reverse card effect.
 * Reverses the direction of play.
 */
public class ReverseEffect implements CardEffect {

    @Override
    public void applyEffect(GameState state) {
        state.reverseDirection();
    }

    @Override
    public String getDescription() {
        return "Reverse direction of play";
    }
}
