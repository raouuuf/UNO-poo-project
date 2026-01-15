package model.strategy;

import model.GameState;

/**
 * Strategy Pattern - Strategy Interface
 * 
 * Defines the contract for different card effects.
 * Each concrete strategy implements a specific card behavior.
 */
public interface CardEffect {
    /**
     * Applies the card's effect to the game state.
     * 
     * @param state The current game state to modify
     */
    void applyEffect(GameState state);

    /**
     * Gets a description of this effect.
     * 
     * @return String description of the effect
     */
    String getDescription();
}
