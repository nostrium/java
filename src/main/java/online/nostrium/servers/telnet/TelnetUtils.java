/*
 * Simple utils for the telnet syntax
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.telnet;

/**
 * @author Brito
 * @date: 7 Oct 2024
 * @location: Germany
 */
public class TelnetUtils {

    /**
     * Generates the characters to delete the given text from a Telnet line.
     * It returns a string that includes backspaces, spaces, and more backspaces
     * to clear the input from the line.
     *
     * @param text The text that needs to be cleared from the Telnet line.
     * @return The sequence of characters to clear the line.
     */
    public static String clearLine(String text) {
        StringBuilder clearSequence = new StringBuilder();

        // Generate backspaces to move the cursor back to the beginning
        for (int i = 0; i < text.length(); i++) {
            clearSequence.append("\b");
        }

        // Generate spaces to overwrite the existing text
        for (int i = 0; i < text.length(); i++) {
            clearSequence.append(" ");
        }

        // Generate backspaces again to move back to the start of the line
        for (int i = 0; i < text.length(); i++) {
            clearSequence.append("\b");
        }

        return clearSequence.toString();
    }
    
}
