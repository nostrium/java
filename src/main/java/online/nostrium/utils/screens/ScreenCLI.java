/*
 * Internal utilities for the text terminals
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.utils.screens;

import online.nostrium.servers.terminal.TerminalColor;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.session.Session;

/**
 * Author: Brito
 * Date: 2024-08-10
 * Location: Germany
 */
public class ScreenCLI extends Screen{

    @Override
    public TerminalType getTerminalType() {
        return TerminalType.PLAIN;
    }

    @Override
    public void writeIntro() {
    }

    @Override
    public void write(String text) {
        System.out.print(text);
    }

    @Override
    public void writeln(String text) {
        System.out.println(text);
    }

    @Override
    public String paint(TerminalColor colorType, String text) {
        return text;
    }

    @Override
    public String getWindowFrame(String title) {
            return title;
    }

    @Override
    public void clearScreen() {
    }

    @Override
    public void deleteCurrentLine() {
    }

    @Override
    public void deletePreviousLine() {
    }

    @Override
    public void writeUserPrompt(Session session) {
    }
    
    @Override
    public void writeLikeHuman(String text, int speed) {
        writeln(text);
    }
    
    @Override
    public String breakLine() {
        return "\n";
    }
}
