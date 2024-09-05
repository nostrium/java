/*
 * Defines an interaction screen with the game
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.util.Scanner;

/**
 * @author Brito
 * @date: 2024-09-04
 * @location: Germany
 */
public class GameScreenCLI extends GameScreen {

    @Override
    public void writeln(String text) {
        
        if(text.isEmpty()){
            System.out.println();
        }
        
        int maxLineLength = 60;

        while (text.length() > maxLineLength) {
            int breakIndex = text.lastIndexOf(' ', maxLineLength);

            // If there's no space, break at maxLineLength (this avoids infinite loops)
            if (breakIndex == -1) {
                breakIndex = maxLineLength;
            }

            // Print the text up to the break point
            System.out.println(text.substring(0, breakIndex));

            // Remove the printed portion from the text and trim leading spaces
            text = text.substring(breakIndex).trim();
        }

        // Print the remaining part (if any)
        if (!text.isEmpty()) {
            System.out.println(text);
        }
    }

    @Override
    public String processCommand(Scene scene) {
        // get the input from the command line
        Scanner scanner = new Scanner(System.in);
        int playerChoice = scanner.nextInt();
        
        if(playerChoice > scene.getChoices().size()){
            writeln("Invalid choice. Please try again.");
            return performChoices(scene);
        }
        // the number is valid, follow the choice
        //String[] choices = scene.getChoices().values().toArray(new String[0]);
        return scene.getChoices().get(playerChoice -1).link;
    }

    @Override
    public String performChoices(Scene scene) {
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

}
