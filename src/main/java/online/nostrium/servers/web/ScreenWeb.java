/*
 *  Define a screen for the web CLI
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.web;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.util.Random;
import online.nostrium.main.core;
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
import static online.nostrium.servers.terminal.TerminalColor.WHITE_ON_GREY;
import static online.nostrium.servers.terminal.TerminalColor.WHITE_ON_RED;
import static online.nostrium.servers.terminal.TerminalColor.WHITE_ON_YELLOW;
import static online.nostrium.servers.terminal.TerminalColor.YELLOW;
import static online.nostrium.servers.terminal.TerminalColor.YELLOW_ON_GREEN;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.servers.terminal.TerminalUtils;
import online.nostrium.utils.screens.Screen;
import static online.nostrium.servers.telnet.ScreenTelnet.ANSI_CLEAR_SCREEN;
import static online.nostrium.servers.telnet.ScreenTelnet.ANSI_HOME;
import static online.nostrium.servers.telnet.ScreenTelnet.ANSI_RESET;
import static online.nostrium.servers.telnet.ScreenTelnet.ANSI_WHITE;
import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-08-27
 * @location: Germany
 */
public class ScreenWeb extends Screen{
    
    final ChannelHandlerContext ctx;

    public ScreenWeb(Session session, ChannelHandlerContext ctx) {
        super(session);
        this.ctx = ctx;
    }

    @Override
    public TerminalType getTerminalType() {
        return TerminalType.ANSI;
    }


    @Override
    public void write(String text) {
        ctx.channel().writeAndFlush(new TextWebSocketFrame(text));
    }

    @Override
    public void writeln(String text) {
        // web terminal needs to always have \r\n
        // so we first remove all cases that might exist
        // and them make them all equal
        text = text.replace("\r\n", "\n");
        text = text.replace("\n", "\r\n");
        
        ctx.channel().writeAndFlush(new TextWebSocketFrame(""
                //+ "\r\n" 
                + text 
                + breakLine()
        ));
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
            write(String.valueOf(c));
            
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
        writeln("");
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

    @Override
    public String getWindowFrame(String title) {
        TerminalColor color = core.config.colorAppsDefault;
           int paddingHorizontal = 4;
        int titleLength = title.length();
        int totalWidth = titleLength + paddingHorizontal * 2; // No extra spaces for the borders

        String output = "";
        String topBottomBorder = "+" + new String(new char[totalWidth]).replace('\0', '-') + "+";
        String line = paint(color, topBottomBorder) + breakLine();
        output += line;

        String text = "|"
                + new String(new char[paddingHorizontal / 2]).replace('\0', ' ')
                + "  "
                + title;

        int dif = topBottomBorder.length() - (text.length() + 1);

        String padding = new String(new char[dif]).replace('\0', ' ');

        line = paint(color, text + padding + "|")
                + breakLine();
        output += line;
        line = paint(color, topBottomBorder);
        output += line;

        return output;
    }

    @Override
    public void clearScreen() {
        write(ANSI_CLEAR_SCREEN + ANSI_HOME);
    }

    @Override
    public void deleteCurrentLine() {
        // ANSI escape code to clear the entire current line and move the cursor to the start of the line
        String text = ""
                + "\u001B[2K" // Clear the current line
                + "\u001B[G"; // Move the cursor to the beginning of the line
        write(text);
    }

    @Override
    public void deletePreviousLine() {
        String text = ""
        // Move the cursor up one line
        + "\u001B[F"
        // Clear the entire current line
        + "\u001B[2K"
        // Move the cursor to the beginning of the line (optional, if you need to print something immediately after)
        + "\u001B[G";
        write(text);
    }

    @Override
    public void writeUserPrompt() {
        String path = TerminalUtils.getPath(session.getApp());

        String userPrompt = paint(GREEN_BRIGHT, 
                session.getUser().getDisplayName())
                + ":"
                + path
                + "> ";
        write(//"\n" + 
                userPrompt);
    }
    
    @Override
    public String breakLine() {
        return "\r\n";
    }

}
