import controller.GameManager;
import view.InputHandler;

/**
 * Main entry point for the UNO game application.
 * Supports both console and GUI modes.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("              Welcome to UNO Game!                         ");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("This game demonstrates:");
        System.out.println("  ✓ MVC Architecture (Model-View-Controller)");
        System.out.println("  ✓ Singleton Pattern (Deck, GameManager, GameSaver)");
        System.out.println("  ✓ Strategy Pattern (Card Effects)");
        System.out.println("  ✓ Composite Pattern (Card Hierarchy)");
        System.out.println("  ✓ Observer Pattern (Game State Updates)");
        System.out.println();
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println();

        InputHandler inputHandler = new InputHandler();

        // Choose mode
        System.out.println("Select game mode:");
        System.out.println("1. Console Mode");
        System.out.println("2. GUI Mode (JavaFX)");
        System.out.print("Enter your choice (1-2): ");

        String choice = new java.util.Scanner(System.in).nextLine().trim();
        boolean useGUI = choice.equals("2");

        // Get game settings
        int playerCount = inputHandler.getPlayerCount();
        int humanCount = inputHandler.getHumanPlayerCount(playerCount);

        // Initialize game
        GameManager manager = GameManager.getInstance();

        if (useGUI) {
            System.out.println("\nLaunching GUI mode (Swing)...");
            manager.initializeGame(playerCount, humanCount, true);
            manager.startGame();
        } else {
            System.out.println("\nStarting Console mode...\n");
            manager.initializeGame(playerCount, humanCount, false);
            manager.startGame();
        }
    }
}
