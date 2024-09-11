/*
 * Internal utilities for the text terminals
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.utils.screens;

import online.nostrium.servers.qoft.QOTD;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalColor;
import static online.nostrium.servers.terminal.TerminalColor.BLUE;
import static online.nostrium.servers.terminal.TerminalColor.GREEN;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.utils.ascii.AsciiArt;

/**
 * Author: Brito
 * Date: 2024-08-10
 * Location: Germany
 */
public abstract class Screen {
    
    
    public abstract TerminalType getTerminalType();
    
    public void writeIntro(){
        writeln(paint(GREEN, AsciiArt.intro()));
        writeln("");
        writeln("");
        writeln(paint(BLUE, "The NOSTR BBS. Type 'help' to list the commands."));
        writeln("");
        writeln(paint(TerminalColor.DARK_GREY_ON_BLACK,"> " + QOTD.generateQuote()));
        writeln("");
        
    }
    
    public abstract void write(String text);
    public abstract void writeln(String text);
    public abstract void writeLikeHuman(String text, int speed);
    
    public abstract String readln();

    public abstract String paint(TerminalColor colorType, String text);
    
    public abstract String getWindowFrame(String title);

    /**
     * Clears the terminal screen
     */
    public abstract void clearScreen();
    
    public abstract void deleteCurrentLine();
    public abstract void deletePreviousLine();
    
    public abstract void writeUserPrompt(TerminalApp app);

    public abstract String breakLine();
    
}
