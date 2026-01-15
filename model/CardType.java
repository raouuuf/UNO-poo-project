package model;

/**
 * Enumeration representing the different types of UNO cards.
 * Each type has different game effects and rules.
 */
public enum CardType {
    NUMBER("Number"),
    SKIP("Skip"),
    REVERSE("Reverse"),
    DRAW_TWO("Draw Two"),
    WILD("Wild"),
    WILD_DRAW_FOUR("Wild Draw Four");

    private final String displayName;

    CardType(String displayName) {
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
