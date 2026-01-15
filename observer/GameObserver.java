package observer;

import model.GameState;

/**
 * Observer Pattern - Observer Interface
 * 
 * Defines the contract for objects that want to be notified
 * when the game state changes.
 */
public interface GameObserver {
    /**
     * Called when the observed GameState changes.
     * 
     * @param state The current game state
     */
    void update(GameState state);
}
