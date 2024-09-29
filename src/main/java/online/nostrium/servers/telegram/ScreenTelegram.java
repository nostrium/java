/*
 * Screen output/input for Telegram
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.telegram;

import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalColor;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.servers.terminal.TerminalUtils;
import online.nostrium.utils.screens.Screen;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Timer;
import java.util.TimerTask;
import online.nostrium.servers.qoft.QOTD;
import online.nostrium.session.Session;
import online.nostrium.utils.ascii.AsciiArt;

/**
 * @author Brito
 * @date: 2024-09-18
 * @location: Germany
 */
public class ScreenTelegram extends Screen {

    private final TelegramClient telegramClient;
    private final long chatId;
    private final StringBuilder messageBuffer; // Buffer for consecutive messages
    private Timer timer; // Timer to delay sending the message

    private static final long MESSAGE_DELAY = 1000; // 1 second delay

    public ScreenTelegram(Session session, TelegramClient telegramClient, long chatId) {
        super(session);
        this.telegramClient = telegramClient;
        this.chatId = chatId;
        this.messageBuffer = new StringBuilder();
        this.timer = new Timer(true); // Timer to handle delayed message sending
    }
    
    @Override
    public void writeIntro(){
        writeln("");
        writeln(AsciiArt.intro());
        writeln("");
        writeln("");
        writeln("The NOSTR BBS. Type 'help' to list the commands.");
        writeln("");
        writeln("> " + QOTD.generateQuote());
        writeln("");
    }

    @Override
    public TerminalType getTerminalType() {
        return TerminalType.PLAIN; // Telegram doesn't support ANSI codes
    }

    @Override
    public void write(String text) {
        bufferText(text);
    }

    @Override
    public void writeln(String text) {
        bufferText(text + "\n");
    }

    @Override
    public void writeLikeHuman(String text, int speed) {
        writeln(text);
    }

    @Override
    public String paint(TerminalColor colorType, String text) {
        return text; // No ANSI color support in Telegram
    }

    @Override
    public String getWindowFrame(String title) {
        return "====== " + title + " ======\n";
    }

    @Override
    public void clearScreen() {
//        sendMessage("---- Clearing screen ----");
    }

    @Override
    public void deleteCurrentLine() {
//        sendMessage("---- Deleting current line ----");
    }

    @Override
    public void deletePreviousLine() {
//        sendMessage("---- Deleting previous line ----");
    }

    @Override
    public void writeUserPrompt(TerminalApp app) {
        String path = TerminalUtils.getPath(app);
        String userPrompt = app.session.getUser().getDisplayName() + ":" + path + "> ";
        writeln(userPrompt);
    }

    @Override
    public String breakLine() {
        return "\n";
    }

    /**
     * Buffers text and sends the combined message after 1 second of inactivity.
     * 
     * @param text Text to be buffered.
     */
    private void bufferText(String text) {
        synchronized (messageBuffer) {
            // Append the text to the buffer
            messageBuffer.append(text);

            // Cancel any existing timer if it's running
            if (timer != null) {
                timer.cancel();
            }

            // Schedule a new timer to send the message after 1 second
            timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (messageBuffer) {
                        sendMessage(messageBuffer.toString()); // Send the message with the combined lines
                        messageBuffer.setLength(0); // Clear the buffer
                    }
                }
            }, MESSAGE_DELAY);
        }
    }

    private void sendMessage(String text) {
        if (text == null || text.trim().isEmpty()) {
            return; // Avoid sending empty messages
        }

        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text("```\n" + text + "```")  // Wrap the text in triple backticks for console-like appearance
                .parseMode("Markdown")       // Specify the parse mode as Markdown
                .build();

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
           // e.printStackTrace();
        }
    }
}
