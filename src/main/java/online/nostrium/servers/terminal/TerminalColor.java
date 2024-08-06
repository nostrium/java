/*
 * Colors common to text messages
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 * 
 * Usage:
 * 
 * To use text colors:
 * System.out.println(TerminalColor.RED.getAnsiCode() + "This is red text" + TerminalColor.RESET.getAnsiCode());
 * System.out.println(TerminalColor.GREEN_BRIGHT.getAnsiCode() + "This is bright green text" + TerminalColor.RESET.getAnsiCode());
 * 
 * To use text and background colors:
 * System.out.println(TerminalColor.RED_ON_BLUE.getAnsiCode() + "This is red text on blue background" + TerminalColor.RESET.getAnsiCode());
 */
package online.nostrium.servers.terminal;

/**
 * Represents ANSI colors for terminal text.
 * 
 * Author: Brito
 * Date: 2024-08-04
 * Location: Germany
 */
public enum TerminalColor {
    // Reset
    RESET("\u001B[0m"),
    
    // Standard text colors
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    MAGENTA("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),
    PURPLE("\u001B[35m"),
    
    // Bright text colors
    BLACK_BRIGHT("\u001B[90m"),
    RED_BRIGHT("\u001B[91m"),
    GREEN_BRIGHT("\u001B[92m"),
    YELLOW_BRIGHT("\u001B[93m"),
    BLUE_BRIGHT("\u001B[94m"),
    MAGENTA_BRIGHT("\u001B[95m"),
    CYAN_BRIGHT("\u001B[96m"),
    WHITE_BRIGHT("\u001B[97m"),

    // Extended text colors
    ORANGE("\u001B[38;5;208m"),
    BROWN("\u001B[38;5;94m"),
    DESERT_SAND("\u001B[38;5;229m"),

    // Text on background combinations
    RED_ON_BLUE("\u001B[31m\u001B[44m"),
    GREEN_ON_YELLOW("\u001B[32m\u001B[43m"),
    ORANGE_ON_DESERT_SAND("\u001B[38;5;208m\u001B[48;5;229m"),
    PURPLE_ON_WHITE("\u001B[35m\u001B[47m"),
    BLUE_ON_RED("\u001B[34m\u001B[41m"),
    CYAN_ON_MAGENTA("\u001B[36m\u001B[45m"),
    YELLOW_ON_GREEN("\u001B[33m\u001B[42m"),
    BROWN_ON_BLACK("\u001B[38;5;94m\u001B[40m"),
    DESERT_SAND_ON_ORANGE("\u001B[38;5;229m\u001B[48;5;208m"),
    WHITE_ON_BLUE("\u001B[37m\u001B[44m"),
    BLACK_ON_WHITE("\u001B[30m\u001B[47m"),
    GREEN_BRIGHT_ON_PURPLE("\u001B[92m\u001B[45m");

    private final String ansiCode;

    TerminalColor(String ansiCode) {
        this.ansiCode = ansiCode;
    }

    public String getAnsiCode() {
        return ansiCode;
    }

    public static String get256ColorCode(int colorCode) {
        if (colorCode < 0 || colorCode > 255) {
            throw new IllegalArgumentException("Color code must be between 0 and 255.");
        }
        return "\u001B[38;5;" + colorCode + "m";
    }

    public static String get256BackgroundColorCode(int colorCode) {
        if (colorCode < 0 || colorCode > 255) {
            throw new IllegalArgumentException("Color code must be between 0 and 255.");
        }
        return "\u001B[48;5;" + colorCode + "m";
    }
}
