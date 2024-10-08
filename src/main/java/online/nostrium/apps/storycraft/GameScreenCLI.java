/*
 * Defines an interaction screen with the game
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.util.Scanner;
import online.nostrium.utils.time;

/**
 * @author Brito
 * @date: 2024-09-04
 * @location: Germany
 */
public class GameScreenCLI extends GameScreen {

    @Override
    public void writeln(String text, String data) {
         System.out.println(text + ": " + data);
    }
    
    @Override
    public void writeTitle(String text) {
        System.out.println("----[ " + text + " ]----");
    }
    
    
    @Override
    public void writeln(String text) {
         System.out.println(text);
//        if (text.isEmpty()) {
//            System.out.println();
//        }
//
//        int maxLineLength = 80;
//
//        while (text.length() > maxLineLength) {
//            int breakIndex = text.lastIndexOf(' ', maxLineLength);
//
//            // If there's no space, break at maxLineLength (this avoids infinite loops)
//            if (breakIndex == -1) {
//                breakIndex = maxLineLength;
//            }
//
//            // Print the text up to the break point
//            System.out.println(text.substring(0, breakIndex));
//
//            // Remove the printed portion from the text and trim leading spaces
//            text = text.substring(breakIndex);//.trim();
//        }
//
//        // Print the remaining part (if any)
//        if (!text.isEmpty()) {
//            System.out.println(text);
//        }
    }

    @Override
    public Choice processCommand(Scene scene) {
        String messageError = "Invalid choice. Please try again.";
        // Get the input from the command line
        Scanner scanner = new Scanner(System.in);
        String input;

        // Read the input as a string to handle both numbers and ENTER key
        input = scanner.nextLine().trim();

        // If the input is empty (ENTER pressed), select the first option
        int playerChoice;
        if (input.isEmpty()) {
            playerChoice = 1;  // Equivalent to the first option
        } else {
            try {
                playerChoice = Integer.parseInt(input);  // Convert input to an integer
            } catch (NumberFormatException e) {
                writeln(messageError);
                return performChoices(scene);  // Invalid input, show choices again
            }
        }

        // Check if the playerChoice is valid
        if (playerChoice < 1 || playerChoice > scene.getChoices().size()) {
            writeln(messageError);
            return performChoices(scene);  // Invalid choice, show choices again
        }

        // The choice is valid, proceed with the selection
        return scene.getChoices().get(playerChoice - 1);
    }

    @Override
    public String processCommand(String... actions) {
        String messageError = "Invalid choice. Please try again.";
        // Get the input from the command line
        Scanner scanner = new Scanner(System.in);
        String input;

        // Read the input as a string to handle both numbers and ENTER key
        input = scanner.nextLine().trim();

        // If the input is empty (ENTER pressed), select the first option
        int playerChoice;
        if (input.isEmpty()) {
            playerChoice = 1;  // Equivalent to the first option
        } else {
            try {
                playerChoice = Integer.parseInt(input);  // Convert input to an integer
            } catch (NumberFormatException e) {
                writeln(messageError);
                return performChoices(actions);  // Invalid input, show choices again
            }
        }

        // Check if the playerChoice is valid
        if (playerChoice < 1 || playerChoice > actions.length) {
            writeln(messageError);
            return performChoices(actions);  // Invalid choice, show choices again
        }

        // The choice is valid, proceed with the selection
        return actions[playerChoice - 1];
    }
    
    @Override
    public Choice performChoices(Scene scene) {
        // is there any choice here to be made?
        if (scene.getChoices().isEmpty()) {
            return null;
        }

        writeln("");
        int i = 1;
        for (Choice choice : scene.getChoices()) {
            writeln("  ("
                    + i
                    + ") " + choice);
            i++;
        }
        writeln("");
        writeln("Your choice: ");

        return processCommand(scene);
    }

    @Override
    public void delay(int i){
        time.waitMs(i);
    }

    @Override
    public String performChoices(String... actions) {
        if (actions == null || actions.length == 0) {
            return null;
        }

        writeln("");
        int i = 1;
        for (String action : actions) {
            writeln("  ("
                    + i
                    + ") " + action);
            i++;
        }
        writeln("");
        writeln("Your choice: ");
        return processCommand(actions);
    }

    @Override
    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
