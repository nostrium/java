/*
 * Internal utilities for the text terminals
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.terminal.screens;

import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalColor;
import online.nostrium.servers.terminal.TerminalType;

/**
 * Author: Brito
 * Date: 2024-08-10
 * Location: Germany
 */
public abstract class Screen {
    
    
    public abstract TerminalType getTerminalType();
    
    public abstract void writeIntro();
    
    public abstract void write(String text);
    public abstract void writeln(String text);
    public abstract void writeLikeHuman(String text, int speed);
    
    public abstract String readln();

    public abstract String paint(TerminalColor colorType, String text);
    
    public abstract String getWindowFrame(TerminalColor color, String title);

    /**
     * Clears the terminal screen
     */
    public abstract void clearScreen();
    
    public abstract void deleteCurrentLine();
    public abstract void deletePreviousLine();
    
    public abstract void writeUserPrompt(TerminalApp app);

    public abstract String breakLine();
    
}
