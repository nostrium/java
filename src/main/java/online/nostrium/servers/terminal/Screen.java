/*
 * Internal utilities for the text terminals
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.terminal;

/**
 * Author: Brito
 * Date: 2024-08-04
 * Location: Germany
 */
public class Screen {
    
       // ANSI escape code to clear the screen
    public static final String 
            ANSI_CLEAR_SCREEN = "\u001B[2J",    
            ANSI_HOME = "\u001B[H";
    
    // ANSI escape codes for clearing the line and moving the cursor
    public static final String
            ANSI_CLEAR_LINE = "\u001B[2K",
            ANSI_CURSOR_TO_LINE_START = "\u001B[G";
   
    
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
    
    public static String paint(TerminalType tType, TerminalColor colorType, String text) {
        String color = ANSI_WHITE; // default color
        
        switch(colorType) {
            case BLACK -> color = TerminalColor.BLACK.getAnsiCode();
            case RED -> color = TerminalColor.RED.getAnsiCode();
            case GREEN -> color = TerminalColor.GREEN.getAnsiCode();
            case GREEN_BRIGHT -> color = TerminalColor.GREEN_BRIGHT.getAnsiCode();
            case YELLOW -> color = TerminalColor.YELLOW.getAnsiCode();
            case BLUE -> color = TerminalColor.BLUE.getAnsiCode();
            case PURPLE -> color = TerminalColor.PURPLE.getAnsiCode();
            case CYAN -> color = TerminalColor.CYAN.getAnsiCode();
            case WHITE -> color = TerminalColor.WHITE.getAnsiCode();
            case ORANGE -> color = TerminalColor.ORANGE.getAnsiCode();
            case BROWN -> color = TerminalColor.BROWN.getAnsiCode();
            case DESERT_SAND -> color = TerminalColor.DESERT_SAND.getAnsiCode();
            case RED_ON_BLUE -> color = TerminalColor.RED_ON_BLUE.getAnsiCode();
            case GREEN_ON_YELLOW -> color = TerminalColor.GREEN_ON_YELLOW.getAnsiCode();
            case ORANGE_ON_DESERT_SAND -> color = TerminalColor.ORANGE_ON_DESERT_SAND.getAnsiCode();
            case PURPLE_ON_WHITE -> color = TerminalColor.PURPLE_ON_WHITE.getAnsiCode();
            case BLUE_ON_RED -> color = TerminalColor.BLUE_ON_RED.getAnsiCode();
            case CYAN_ON_MAGENTA -> color = TerminalColor.CYAN_ON_MAGENTA.getAnsiCode();
            case YELLOW_ON_GREEN -> color = TerminalColor.YELLOW_ON_GREEN.getAnsiCode();
            case BROWN_ON_BLACK -> color = TerminalColor.BROWN_ON_BLACK.getAnsiCode();
            case DESERT_SAND_ON_ORANGE -> color = TerminalColor.DESERT_SAND_ON_ORANGE.getAnsiCode();
            case WHITE_ON_BLUE -> color = TerminalColor.WHITE_ON_BLUE.getAnsiCode();
            case BLACK_ON_WHITE -> color = TerminalColor.BLACK_ON_WHITE.getAnsiCode();
            case GREEN_BRIGHT_ON_PURPLE -> color = TerminalColor.GREEN_BRIGHT_ON_PURPLE.getAnsiCode();
        }
        
        // output according to color
        if(tType == TerminalType.ANSI){
            return color + text + ANSI_RESET;
        }
        
        return text;
    }

    
    /**
     * Clears the terminal screen.
     *
     * @return 
     */
    public static String clearScreen() {
        return ANSI_CLEAR_SCREEN + ANSI_HOME;
    }
    
}
