/*
 * Detect when copy and paste is happening
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.terminal.telnet;

public class TelnetDetectCopyAndPaste {

    private StringBuilder accumulatedLine = new StringBuilder();
    private long lastInputTime = System.currentTimeMillis();
    private boolean isPasting = false;
    private final long PASTE_THRESHOLD_MS = 200;  // Threshold time in milliseconds

    /**
     * Add a new character to the current input.
     * Detects and accumulates the input for processing.
     * @param currentChar 
     */
    public void append(char currentChar) {
        long currentTime = System.currentTimeMillis();

        // Determine if we are in a paste operation based on timing
        if ((currentTime - lastInputTime) < PASTE_THRESHOLD_MS) {
            isPasting = true;
        } else {
            isPasting = false;
        }

        // Append the character to the accumulated input
        accumulatedLine.append(currentChar);

        // Update the last input time
        lastInputTime = currentTime;
    }

    /**
     * Determines if the input is ready for processing.
     * @return boolean indicating whether the input is ready to be processed.
     */
    public boolean isReadyForProcessing() {
        // Consider the input ready for processing if we're not pasting, or if a newline is detected
        return !isPasting && accumulatedLine.length() > 0 && accumulatedLine.charAt(accumulatedLine.length() - 1) == '\n';
    }

    /**
     * Retrieves the accumulated input and resets the buffer.
     * Resets the command completion status for the next input.
     * @return String containing the complete input command.
     */
    public String getOutputAndReset() {
        String output = accumulatedLine.toString();
        accumulatedLine.setLength(0);
        return output;
    }
}
