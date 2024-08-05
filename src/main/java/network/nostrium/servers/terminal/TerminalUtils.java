/*
 * Internal utilities for the text terminals
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package network.nostrium.servers.terminal;


/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class TerminalUtils {
    
    // ANSI escape codes for colors
    public static final String
            ANSI_RESET = "\u001B[0m",
            ANSI_RED = "\u001B[31m",
            ANSI_GREEN = "\u001B[32m",
            ANSI_GREEN_BRIGHT = "\u001B[32;1m",//"\u001B[92m",
            ANSI_YELLOW = "\u001B[33m",
            ANSI_BLUE = "\u001B[34m",
            ANSI_PURPLE = "\u001B[35m",
            ANSI_CYAN = "\u001B[36m",
            ANSI_WHITE = "\u001B[37m";
    
    
    public static String paint(TerminalType tType, TerminalColor colorType, String text){
        
        // set the default color
        String color = ANSI_WHITE;
        
        switch(colorType){
            case RED -> color = ANSI_RED;
            case GREEN -> color = ANSI_GREEN;
            case GREEN_BRIGHT -> color = ANSI_GREEN_BRIGHT;
            case YELLOW -> color = ANSI_YELLOW;
            case BLUE -> color = ANSI_BLUE;
            case PURPLE -> color = ANSI_PURPLE;
            case CYAN -> color = ANSI_CYAN;
            case WHITE -> color = ANSI_WHITE;
        }
        
        // output according to color
        if(tType == TerminalType.ANSI){
            return color + text + ANSI_RESET;
        }
        
        return text;
    }
    
}
