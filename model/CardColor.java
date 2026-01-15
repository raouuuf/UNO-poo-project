package model;

/**
 * Enumeration representing the possible colors of UNO cards.
 * Used for matching cards and determining valid plays.
 */
public enum CardColor {
    RED("Red"),
    BLUE("Blue"),
    GREEN("Green"),
    YELLOW("Yellow"),
    WILD("Wild");

    private final String displayName;

    CardColor(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
