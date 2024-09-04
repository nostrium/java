/*
 * Handles the game loop and player interactions in the text-based adventure game.
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class GamePlayer {

    private Map<String, Scene> scenes;
    private Scene currentScene;
    private Scanner scanner;

    /**
     * Constructor for the GamePlayer class.
     *
     * @param scenes the map of scenes to play through
     */
    public GamePlayer(Map<String, Scene> scenes) {
        this.scenes = scenes;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the game at the specified scene.
     *
     * @param startSceneId the identifier of the starting scene
     */
    public void start(String startSceneId) {
        currentScene = scenes.get(startSceneId);

        while (currentScene != null) {
            // Display the scene description
            System.out.println(currentScene.getDescription());
            System.out.println("\nChoices:");

            // Display the choices
            if (!currentScene.getChoices().isEmpty()) {
                int choiceIndex = 1;
                for (String choice : currentScene.getChoices()) {
                    System.out.println(choiceIndex + ": " + formatChoice(choice));
                    choiceIndex++;
                }

                // Get the player's choice
                System.out.print("\nYour choice: ");
                int playerChoice = scanner.nextInt();

                // Process the player's choice
                if (playerChoice > 0 && playerChoice <= currentScene.getChoices().size()) {
                    String chosenOption = currentScene.getChoices().get(playerChoice - 1);
                    processChoice(chosenOption);
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }

                // Move to the next scene
                if (currentScene.getNextScene() != null) {
                    currentScene = scenes.get(currentScene.getNextScene());
                } else {
                    currentScene = null;
                }
            } else {
                // If there are no choices, move directly to the next scene
                if (currentScene.getNextScene() != null) {
                    currentScene = scenes.get(currentScene.getNextScene());
                } else {
                    currentScene = null;
                }
            }
        }

        System.out.println("The End.");
    }

    /**
     * Formats the choice string for display.
     *
     * @param choice the raw choice string
     * @return the formatted choice string
     */
    private String formatChoice(String choice) {
        if (choice.startsWith("take-")) {
            return "Take " + choice.substring(5);
        } else if (choice.startsWith("leave-")) {
            return "Leave the item";
        } else if (choice.startsWith("continue-")) {
            return "Continue to the next scene";
        }
        return choice;
    }

    /**
     * Processes the player's chosen option.
     *
     * @param chosenOption the option chosen by the player
     */
    private void processChoice(String chosenOption) {
        if (chosenOption.startsWith("take")) {
            System.out.println("You took the item.");
        } else if (chosenOption.startsWith("leave")) {
            System.out.println("You left the item.");
        } else if (chosenOption.startsWith("continue")) {
            System.out.println("You continue to the next scene.");
        }
    }

    public static void main(String[] args) {
        try {
            GameParser parser = new GameParser();
            Map<String, Scene> scenes = parser.parseScript(StoryExample.text); // Parse from a string for this example
            GamePlayer player = new GamePlayer(scenes);
            player.start("azurath-entrance"); // Start the game at the entrance
        } catch (IOException e) {
            System.out.println("Failed to load the script: " + e.getMessage());
        }
    }
}
