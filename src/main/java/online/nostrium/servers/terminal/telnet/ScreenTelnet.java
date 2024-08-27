/*
 * Internal utilities for the text terminals
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.terminal.telnet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalColor;
import static online.nostrium.servers.terminal.TerminalColor.BLUE;
import static online.nostrium.servers.terminal.TerminalColor.GREEN;
import static online.nostrium.servers.terminal.TerminalColor.GREEN_BRIGHT;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.servers.terminal.TerminalUtils;
import online.nostrium.servers.terminal.screens.Screen;
import online.nostrium.utils.AsciiArt;

/**
 * Author: Brito
 * Date: 2024-08-10
 * Location: Germany
 */
public class ScreenTelnet extends Screen {

    final InputStream in; // Changed from BufferedReader to InputStream
    final PrintWriter out;

    // ANSI escape code to clear the screen
    public static final String 
            ANSI_CLEAR_SCREEN = "\u001B[2J",    
            ANSI_HOME = "\u001B[H";
    
    // ANSI escape codes for clearing the line and moving the cursor
    public static final String
            ANSI_CLEAR_LINE = "\\u0007",
            ANSI_CURSOR_TO_LINE_START = "\u001B[G";
   
    // ANSI code for sound
    public static final String
            ANSI_BELL = "\u001B[2K";
   
    // ANSI escape codes for colors
    public static final String
            ANSI_RESET = "\u001B[0m",
            ANSI_RED = "\u001B[31m",
            ANSI_GREEN = "\u001B[32m",
            ANSI_GREEN_BRIGHT = "\u001B[92m",
            ANSI_YELLOW = "\u001B[33m",
            ANSI_BLUE = "\u001B[34m",
            ANSI_PURPLE = "\u001B[35m",
            ANSI_CYAN = "\u001B[36m",
            ANSI_WHITE = "\u001B[37m",
            ANSI_ORANGE = "\u001B[38;5;208m",
            ANSI_BROWN = "\u001B[38;5;94m",
            ANSI_DESERT_SAND = "\u001B[38;5;229m";

    public ScreenTelnet(InputStream in, PrintWriter out) { // Modified constructor to accept InputStream instead of BufferedReader
        this.in = in;
        this.out = out;
    }

    @Override
    public TerminalType getTerminalType() {
        return TerminalType.ANSI;
    }

    @Override
    public void write(String text) {
        out.print(text);
    }

    @Override
    public void writeln(String text) {
        out.println(text);
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void writeLikeHuman(String text, int speed) {
        Random random = new Random();
        int length = text.length();
        
        // Calculate the maximum time to display the text (now 2000ms or 2 seconds)
        int maxDisplayTime = 1000;
        
        // Determine the total delay based on the length of the text and adjust dynamically
        int totalDelay = length * speed;
        int adjustedSpeed = speed;

        if (totalDelay > maxDisplayTime) {
            // Adjust speed to ensure the entire text is shown within the maximum display time
            adjustedSpeed = Math.max(10, maxDisplayTime / length);
        }

        for (char c : text.toCharArray()) {
            out.write(c);
            out.flush();

            try {
                // Base delay for each character
                int delay = random.nextInt(100) + adjustedSpeed;
                
                // Extra pause for spaces
                if (c == ' ') {
                    delay += 100; // Increase delay for spaces
                }

                // Sleep for the calculated delay
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return; // Exit the method if the thread is interrupted
            }
        }

        // Add an end line
        out.write("\n");
        out.flush();
    }

    @Override
    public String readln() {
        StringBuilder inputLine = new StringBuilder();
        try {
            int inputChar;
            while ((inputChar = in.read()) != -1) {
                char receivedChar = (char) inputChar;

                // If newline or carriage return is detected, break the loop
                if (receivedChar == '\n' || receivedChar == '\r') {
                    break;
                }

                inputLine.append(receivedChar);
            }
        } catch (IOException ex) {
            Logger.getLogger(ScreenTelnet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inputLine.toString();
    }

    @Override
    public String paint(TerminalColor colorType, String text) {
        String color = ANSI_WHITE; // default color

        switch (colorType) {
            case BLACK ->
                color = TerminalColor.BLACK.getAnsiCode();
            case RED ->
                color = TerminalColor.RED.getAnsiCode();
            case GREEN ->
                color = TerminalColor.GREEN.getAnsiCode();
            case GREEN_BRIGHT ->
                color = TerminalColor.GREEN_BRIGHT.getAnsiCode();
            case YELLOW ->
                color = TerminalColor.YELLOW.getAnsiCode();
            case BLUE ->
                color = TerminalColor.BLUE.getAnsiCode();
            case PURPLE ->
                color = TerminalColor.PURPLE.getAnsiCode();
            case CYAN ->
                color = TerminalColor.CYAN.getAnsiCode();
            case WHITE ->
                color = TerminalColor.WHITE.getAnsiCode();
            case ORANGE ->
                color = TerminalColor.ORANGE.getAnsiCode();
            case BROWN ->
                color = TerminalColor.BROWN.getAnsiCode();
            case DESERT_SAND ->
                color = TerminalColor.DESERT_SAND.getAnsiCode();
            case RED_ON_BLUE ->
                color = TerminalColor.RED_ON_BLUE.getAnsiCode();
            case GREEN_ON_YELLOW ->
                color = TerminalColor.GREEN_ON_YELLOW.getAnsiCode();
            case ORANGE_ON_DESERT_SAND ->
                color = TerminalColor.ORANGE_ON_DESERT_SAND.getAnsiCode();
            case PURPLE_ON_WHITE ->
                color = TerminalColor.PURPLE_ON_WHITE.getAnsiCode();
            case BLUE_ON_RED ->
                color = TerminalColor.BLUE_ON_RED.getAnsiCode();
            case CYAN_ON_MAGENTA ->
                color = TerminalColor.CYAN_ON_MAGENTA.getAnsiCode();
            case YELLOW_ON_GREEN ->
                color = TerminalColor.YELLOW_ON_GREEN.getAnsiCode();
            case BROWN_ON_BLACK ->
                color = TerminalColor.BROWN_ON_BLACK.getAnsiCode();
            case DESERT_SAND_ON_ORANGE ->
                color = TerminalColor.DESERT_SAND_ON_ORANGE.getAnsiCode();
            case BLACK_ON_WHITE ->
                color = TerminalColor.BLACK_ON_WHITE.getAnsiCode();
            case GREEN_BRIGHT_ON_PURPLE ->
                color = TerminalColor.GREEN_BRIGHT_ON_PURPLE.getAnsiCode();
            
            // notifications
            case WHITE_ON_BLUE ->
                color = TerminalColor.WHITE_ON_BLUE.getAnsiCode();
            case WHITE_ON_YELLOW ->
                color = TerminalColor.WHITE_ON_YELLOW.getAnsiCode();
            case WHITE_ON_RED ->
                color = TerminalColor.WHITE_ON_RED.getAnsiCode();
            case WHITE_ON_GREY ->
                color = TerminalColor.WHITE_ON_GREY.getAnsiCode();
        }

        // output according to color
        return color + text + ANSI_RESET;
    }

    /**
     * Returns an ASCII art window frame around the given title.
     *
     * @param title The title to be framed.
     * @param color
     * @return The framed title as a string.
     */
    @Override
    public String getWindowFrame(TerminalColor color, String title) {
        int paddingHorizontal = 4;
        int titleLength = title.length();
        int totalWidth = titleLength + paddingHorizontal * 2; // No extra spaces for the borders

        String output = "";
        String topBottomBorder = "+" + new String(new char[totalWidth]).replace('\0', '-') + "+";
        String line = paint(color, topBottomBorder) + "\n";
        output += line;

        String text = "|"
                + new String(new char[paddingHorizontal / 2]).replace('\0', ' ')
                + "  "
                + title;

        int dif = topBottomBorder.length() - (text.length() + 1);

        String padding = new String(new char[dif]).replace('\0', ' ');

        line = paint(color, text + padding + "|")
                + "\n";
        output += line;
        line = paint(color, topBottomBorder);
        output += line;

        return output;
    }

    @Override
    public void clearScreen() {
        out.write(ANSI_CLEAR_SCREEN + ANSI_HOME);
    }

    @Override
    public void writeIntro() {
        writeln(paint(GREEN, AsciiArt.intro()));
        writeln("");
        writeln("");
        writeln(paint(BLUE, "The NOSTR BBS. Type '/help' to list the commands."));
        writeln("");
    }

    @Override
    public void writeUserPrompt(TerminalApp app) {
        String path = TerminalUtils.getPath(app);

        String userPrompt = paint(GREEN_BRIGHT, app.user.getDisplayName())
                + ":"
                + path
                + "> ";
        out.print(userPrompt);
        out.flush();
    }

    /**
     * Deletes the current line on the telnet screen.
     */
    @Override
    public void deleteCurrentLine() {
        // ANSI escape code to clear the entire current line and move the cursor to the start of the line
        out.write("\u001B[2K");  // Clear the current line
        out.write("\u001B[G");   // Move the cursor to the beginning of the line
        out.flush();
    }

    /**
     * Deletes the previous line on the telnet screen.
     */
    @Override
    public void deletePreviousLine() {
        // Move the cursor up one line
        out.write("\u001B[F");
        // Clear the entire current line
        out.write("\u001B[2K");
        // Move the cursor to the beginning of the line (optional, if you need to print something immediately after)
        out.write("\u001B[G");
        out.flush();
    }
}
