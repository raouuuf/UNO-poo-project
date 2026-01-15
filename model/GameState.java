package model;

import observer.GameObserver;
import java.util.ArrayList;
import java.util.List;

/**
 * Observer Pattern - Subject
 * 
 * Manages the current state of the UNO game.
 * Notifies observers when the state changes.
 */
public class GameState {
    private List<Player> players;
    private int currentPlayerIndex;
    private boolean clockwise;
    private Card topCard;
    private int pendingDrawCount;
    private boolean colorChangeNeeded;
    private List<GameObserver> observers;

    /**
     * Constructor for GameState.
     */
    public GameState() {
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.clockwise = true;
        this.pendingDrawCount = 0;
        this.colorChangeNeeded = false;
        this.observers = new ArrayList<>();
    }

    /**
     * Adds an observer to be notified of state changes.
     * 
     * @param observer The observer to add
     */
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer.
     * 
     * @param observer The observer to remove
     */
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers of a state change.
     */
    public void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.update(this);
        }
    }

    /**
     * Advances to the next player's turn.
     */
    public void advanceTurn() {
        if (clockwise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
        notifyObservers();
    }

    /**
     * Reverses the direction of play.
     */
    public void reverseDirection() {
        clockwise = !clockwise;
        // In a 2-player game, reverse acts like skip
        if (players.size() == 2) {
            advanceTurn();
        }
    }

    /**
     * Gets the current player.
     * 
     * @return The current player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Gets the next player (without advancing the turn).
     * 
     * @return The next player
     */
    public Player getNextPlayer() {
        int nextIndex;
        if (clockwise) {
            nextIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            nextIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
        return players.get(nextIndex);
    }

    // Getters and Setters
    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int index) {
        this.currentPlayerIndex = index;
    }

    public boolean isClockwise() {
        return clockwise;
    }

    public void setClockwise(boolean clockwise) {
        this.clockwise = clockwise;
    }

    public Card getTopCard() {
        return topCard;
    }

    public void setTopCard(Card topCard) {
        this.topCard = topCard;
        notifyObservers();
    }

    public int getPendingDrawCount() {
        return pendingDrawCount;
    }

    public void setPendingDrawCount(int count) {
        this.pendingDrawCount = count;
    }

    public boolean isColorChangeNeeded() {
        return colorChangeNeeded;
    }

    public void setColorChangeNeeded(boolean needed) {
        this.colorChangeNeeded = needed;
    }

    public String getDirectionSymbol() {
        return clockwise ? "→" : "←";
    }
}
