package model.strategy;

import model.GameState;

/**
 * Strategy Pattern - Concrete Strategy
 * 
 * Implements the Wild card effect.
 * Allows the player to choose a new color.
 */
public class WildEffect implements CardEffect {

    @Override
    public void applyEffect(GameState state) {
        // The color change is handled by the controller
        // This effect just marks that a color change is needed
        state.setColorChangeNeeded(true);
    }

    @Override
    public String getDescription() {
        return "Choose a new color";
    }
}
