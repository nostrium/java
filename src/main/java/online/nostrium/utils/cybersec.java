/*
 * Handy functions to clean inputs and output
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.utils;

/**
 * @author Brito
 * @date: 2024-08-29
 * @location: Germany
 */
public class cybersec {
    
    /**
     * Validates the input string.
     *
     * The input must:
     * - Not be null or empty.
     * - Be no longer than 128 characters.
     * - Contain only alphanumeric characters and the following special characters: _-@.:/
     * - Contain only ASCII characters within the printable range (32-126).
     *
     * @param input The input string to validate.
     * @return true if the input is valid, false if invalid.
     */
    public static boolean checkRequest(String input) {
        // Check if input is null or empty
        if (input == null || input.isEmpty()) {
            return false;
        }

        // Check the length of the input
        if (input.length() > 128) {
            return false;
        }

        // Regular expression to match allowed characters (alphanumeric and _-@.:/)
        String allowedCharsRegex = "^[a-zA-Z0-9_\\-@.:/]+$";

        // Check if the input contains only allowed characters
        if (!input.matches(allowedCharsRegex)) {
            return false;
        }

        // Ensure all characters are within the ASCII range (32-126)
        for (char c : input.toCharArray()) {
            if (c < 32 || c > 126) {
                return false;
            }
        }

        // If all checks pass, return true
        return true;
    }

}
