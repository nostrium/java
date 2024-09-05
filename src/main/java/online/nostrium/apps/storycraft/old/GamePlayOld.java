/*
 * Handles the game loop and player interactions in the text-based adventure game.
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft.old;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import online.nostrium.apps.storycraft.Scene;
import online.nostrium.apps.storycraft.examples.StoryExample;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class GamePlayOld {

    private Map<String, Scene> scenes;
    private Scene currentScene;
    private Scanner scanner;

    /**
     * Constructor for the GamePlayer class.
     *
     * @param scenes the map of scenes to play through
     */
    public GamePlayOld(Map<String, Scene> scenes) {
        this.scenes = scenes;
        this.scanner = new Scanner(System.in);
    }

    GamePlayOld(String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * Starts the game at the specified scene.
     *
     * @param sceneId the identifier of the starting scene
     */
    @SuppressWarnings("empty-statement")
    public void play(String sceneId) {
        currentScene = scenes.get(sceneId);

        while (currentScene != null) {
            // Display the scene description
            System.out.println(currentScene.getDescription());
            System.out.println("\nChoices:");

            // Display the choices
//            if (!currentScene.getChoices().isEmpty()) {
//                int choiceIndex = 1;
//                for (String choice : currentScene.getChoices().keySet()) {
//                    System.out.println(choiceIndex + ": " + formatChoice(choice));
//                    choiceIndex++;
//                }
//
//                // Get the player's choice
//                System.out.print("\nYour choice: ");
//                int playerChoice = scanner.nextInt();
//
//                // Process the player's choice
//                if (playerChoice > 0 && playerChoice <= currentScene.getChoices().size()) {
//                    String[] options = currentScene.getChoices().values().toArray(new String[0]);;
//                    String chosenOption = options[playerChoice - 1];
//                    processChoice(chosenOption);
//                } else {
//                    System.out.println("Invalid choice. Please try again.");
//                }
//
//                // Move to the next scene
//                if (currentScene.getNextScene() != null) {
//                    currentScene = scenes.get(currentScene.getNextScene());
//                } else {
//                    currentScene = null;
//                }
//            } else {
//                // If there are no choices, move directly to the next scene
//                if (currentScene.getNextScene() != null) {
//                    currentScene = scenes.get(currentScene.getNextScene());
//                } else {
//                    currentScene = null;
//                }
//            }
//        }

        System.out.println("The End.");
    }

//    /**
//     * Formats the choice string for display.
//     *
//     * @param choice the raw choice string
//     * @return the formatted choice string
//     */
//    private String formatChoice(String choice) {
//        if (choice.startsWith("take-")) {
//            return "Take " + choice.substring(5);
//        } else if (choice.startsWith("leave-")) {
//            return "Leave the item";
//        } else if (choice.startsWith("continue-")) {
//            return "Continue to the next scene";
//        }
//        return choice;
//    }

//    /**
//     * Processes the player's chosen option.
//     *
//     * @param chosenOption the option chosen by the player
//     */
//    private void processChoice(String chosenOption) {
//        if (chosenOption.startsWith("take")) {
//            System.out.println("You took the item.");
//        } else if (chosenOption.startsWith("leave")) {
//            System.out.println("You left the item.");
//        } else if (chosenOption.startsWith("continue")) {
//            System.out.println("You continue to the next scene.");
//        }
//        // follow to another chapter
//        Scene scene = scenes.get(chosenOption);
//        if(scene != null){
//            this.currentScene = scene;
//        }
//        //play(chosenOption);
//    }

//    public static void main(String[] args) {
//        try {
//            GameParserOld parser = new GameParserOld();
//            Map<String, Scene> scenes = parser.parseScript(StoryExample.text); // Parse from a string for this example
//            GamePlayOld game = new GamePlayOld(scenes);
//            game.play("azurath-entrance"); // Start the game at the entrance
//        } catch (IOException e) {
//            System.out.println("Failed to load the script: " + e.getMessage());
//        }
    }
}
