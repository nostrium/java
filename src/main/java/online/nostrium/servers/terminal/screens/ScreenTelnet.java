/*
 * Internal utilities for the text terminals
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.terminal.screens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import static online.nostrium.servers.terminal.ScreenANSI.ANSI_CLEAR_SCREEN;
import static online.nostrium.servers.terminal.ScreenANSI.ANSI_HOME;
import static online.nostrium.servers.terminal.ScreenANSI.ANSI_RESET;
import static online.nostrium.servers.terminal.ScreenANSI.ANSI_WHITE;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalColor;
import static online.nostrium.servers.terminal.TerminalColor.BLACK;
import static online.nostrium.servers.terminal.TerminalColor.BLACK_ON_WHITE;
import static online.nostrium.servers.terminal.TerminalColor.BLUE;
import static online.nostrium.servers.terminal.TerminalColor.BLUE_ON_RED;
import static online.nostrium.servers.terminal.TerminalColor.BROWN;
import static online.nostrium.servers.terminal.TerminalColor.BROWN_ON_BLACK;
import static online.nostrium.servers.terminal.TerminalColor.CYAN;
import static online.nostrium.servers.terminal.TerminalColor.CYAN_ON_MAGENTA;
import static online.nostrium.servers.terminal.TerminalColor.DESERT_SAND;
import static online.nostrium.servers.terminal.TerminalColor.DESERT_SAND_ON_ORANGE;
import static online.nostrium.servers.terminal.TerminalColor.GREEN;
import static online.nostrium.servers.terminal.TerminalColor.GREEN_BRIGHT;
import static online.nostrium.servers.terminal.TerminalColor.GREEN_BRIGHT_ON_PURPLE;
import static online.nostrium.servers.terminal.TerminalColor.GREEN_ON_YELLOW;
import static online.nostrium.servers.terminal.TerminalColor.ORANGE;
import static online.nostrium.servers.terminal.TerminalColor.ORANGE_ON_DESERT_SAND;
import static online.nostrium.servers.terminal.TerminalColor.PURPLE;
import static online.nostrium.servers.terminal.TerminalColor.PURPLE_ON_WHITE;
import static online.nostrium.servers.terminal.TerminalColor.RED;
import static online.nostrium.servers.terminal.TerminalColor.RED_ON_BLUE;
import static online.nostrium.servers.terminal.TerminalColor.WHITE;
import static online.nostrium.servers.terminal.TerminalColor.WHITE_ON_BLUE;
import static online.nostrium.servers.terminal.TerminalColor.YELLOW;
import static online.nostrium.servers.terminal.TerminalColor.YELLOW_ON_GREEN;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.servers.terminal.TerminalUtils;
import online.nostrium.users.User;
import online.nostrium.utils.AsciiArt;

/**
 * Author: Brito Date: 2024-08-10 Location: Germany
 */
public class ScreenTelnet extends Screen {

    final BufferedReader in;
    final PrintWriter out;

    public ScreenTelnet(BufferedReader in, PrintWriter out) {
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
    public String readln() {
        String text;
        try {
            text = in.readLine();
            return text;
        } catch (IOException ex) {
            Logger.getLogger(ScreenTelnet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
            case WHITE_ON_BLUE ->
                color = TerminalColor.WHITE_ON_BLUE.getAnsiCode();
            case BLACK_ON_WHITE ->
                color = TerminalColor.BLACK_ON_WHITE.getAnsiCode();
            case GREEN_BRIGHT_ON_PURPLE ->
                color = TerminalColor.GREEN_BRIGHT_ON_PURPLE.getAnsiCode();
        }

        // output according to color
        return color + text + ANSI_RESET;
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
    public void writeUserPrompt(TerminalApp app, User user) {
         String path = TerminalUtils.getPath(app);

        String userPrompt = paint(GREEN_BRIGHT,user.getDisplayName())
                + ":"
                + path
                + "> ";
        out.print(userPrompt);
        out.flush();
    }

}
