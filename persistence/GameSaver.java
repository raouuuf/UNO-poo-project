package persistence;

import model.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Singleton Pattern - Manages game save/load operations.
 * 
 * Handles file-based persistence of game state using simple text format.
 */
public class GameSaver {
    private static GameSaver instance;
    private static final String SAVE_DIRECTORY = "saves";

    /**
     * Private constructor for Singleton pattern.
     */
    private GameSaver() {
        createSaveDirectory();
    }

    /**
     * Gets the singleton instance of GameSaver.
     * 
     * @return The single GameSaver instance
     */
    public static GameSaver getInstance() {
        if (instance == null) {
            instance = new GameSaver();
        }
        return instance;
    }

    /**
     * Creates the save directory if it doesn't exist.
     */
    private void createSaveDirectory() {
        try {
            Files.createDirectories(Paths.get(SAVE_DIRECTORY));
        } catch (IOException e) {
            System.err.println("Failed to create save directory: " + e.getMessage());
        }
    }

    /**
     * Saves the current game state to a file.
     * 
     * @param gameState The game state to save
     * @param saveName  Optional custom save name
     * @return true if save was successful
     */
    public boolean saveGame(GameState gameState, String saveName) {
        try {
            String fileName = generateFileName(saveName);
            String filePath = SAVE_DIRECTORY + File.separator + fileName;

            // Simple text-based save format
            StringBuilder sb = new StringBuilder();
            sb.append("UNO_SAVE_V1\n");
            sb.append("CurrentPlayerIndex:").append(gameState.getCurrentPlayerIndex()).append("\n");
            sb.append("Clockwise:").append(gameState.isClockwise()).append("\n");
            sb.append("PendingDrawCount:").append(gameState.getPendingDrawCount()).append("\n");

            Files.write(Paths.get(filePath), sb.toString().getBytes());
            System.out.println("Game saved successfully to: " + fileName);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads a game state from a file.
     * 
     * @param fileName The name of the save file
     * @return The loaded game state, or null if failed
     */
    public GameState loadGame(String fileName) {
        try {
            String filePath = SAVE_DIRECTORY + File.separator + fileName;
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            GameState state = new GameState();
            for (String line : lines) {
                if (line.startsWith("CurrentPlayerIndex:")) {
                    int index = Integer.parseInt(line.split(":")[1]);
                    state.setCurrentPlayerIndex(index);
                } else if (line.startsWith("Clockwise:")) {
                    boolean clockwise = Boolean.parseBoolean(line.split(":")[1]);
                    state.setClockwise(clockwise);
                } else if (line.startsWith("PendingDrawCount:")) {
                    int count = Integer.parseInt(line.split(":")[1]);
                    state.setPendingDrawCount(count);
                }
            }

            System.out.println("Game loaded successfully from: " + fileName);
            return state;
        } catch (IOException e) {
            System.err.println("Failed to load game: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets a list of available save files.
     * 
     * @return List of save file names
     */
    public List<String> getAvailableSaves() {
        List<String> saves = new ArrayList<>();
        try {
            Files.list(Paths.get(SAVE_DIRECTORY))
                    .filter(path -> path.toString().endsWith(".txt"))
                    .forEach(path -> saves.add(path.getFileName().toString()));
        } catch (IOException e) {
            System.err.println("Failed to list saves: " + e.getMessage());
        }
        return saves;
    }

    /**
     * Generates a file name for a save.
     * 
     * @param customName Optional custom name
     * @return The generated file name
     */
    private String generateFileName(String customName) {
        if (customName != null && !customName.isEmpty()) {
            return customName + ".txt";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return "uno_save_" + LocalDateTime.now().format(formatter) + ".txt";
    }
}
