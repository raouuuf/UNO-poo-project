package view;

import java.util.Scanner;
import model.CardColor;

/**
 * Handles user input from the console.
 * Validates and processes player choices.
 */
public class InputHandler {
    private final Scanner scanner;

    /**
     * Constructor for InputHandler.
     */
    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Gets the player's choice of card to play.
     * 
     * @param maxIndex The maximum valid index
     * @return The chosen card index, or -1 to draw
     */
    public int getCardChoice(int maxIndex) {
        while (true) {
            System.out.print("Enter card number to play (or 0 to draw a card): ");
            try {
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);

                if (choice == 0) {
                    return -1; // Draw a card
                }

                if (choice >= 1 && choice <= maxIndex) {
                    return choice - 1; // Convert to 0-indexed
                }

                System.out.println("Invalid choice. Please enter a number between 0 and " + maxIndex);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Gets the player's choice of color for a wild card.
     * 
     * @return The chosen color
     */
    public CardColor getColorChoice() {
        System.out.println("\nChoose a color:");
        System.out.println("1. Red");
        System.out.println("2. Blue");
        System.out.println("3. Green");
        System.out.println("4. Yellow");

        while (true) {
            System.out.print("Enter your choice (1-4): ");
            try {
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
                        return CardColor.RED;
                    case 2:
                        return CardColor.BLUE;
                    case 3:
                        return CardColor.GREEN;
                    case 4:
                        return CardColor.YELLOW;
                    default:
                        System.out.println("Invalid choice. Please enter 1-4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Gets the number of players for the game.
     * 
     * @return The number of players (2-10)
     */
    public int getPlayerCount() {
        while (true) {
            System.out.print("Enter number of players (2-10): ");
            try {
                String input = scanner.nextLine().trim();
                int count = Integer.parseInt(input);

                if (count >= 2 && count <= 10) {
                    return count;
                }

                System.out.println("Invalid number. Please enter a number between 2 and 10.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Gets the number of human players.
     * 
     * @param totalPlayers The total number of players
     * @return The number of human players
     */
    public int getHumanPlayerCount(int totalPlayers) {
        while (true) {
            System.out.print("Enter number of human players (1-" + totalPlayers + "): ");
            try {
                String input = scanner.nextLine().trim();
                int count = Integer.parseInt(input);

                if (count >= 1 && count <= totalPlayers) {
                    return count;
                }

                System.out.println("Invalid number. Please enter a number between 1 and " + totalPlayers);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Waits for the user to press Enter.
     */
    public void waitForEnter() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Gets a yes/no answer from the user.
     * 
     * @param prompt The question to ask
     * @return true for yes, false for no
     */
    public boolean getYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }

            System.out.println("Invalid input. Please enter 'y' or 'n'.");
        }
    }

    /**
     * Closes the scanner.
     */
    public void close() {
        scanner.close();
    }
}
