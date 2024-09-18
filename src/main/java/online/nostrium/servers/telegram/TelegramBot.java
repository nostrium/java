/*
 * Telegram bot to interact with users
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.telegram;

import java.util.HashMap;
import online.nostrium.apps.basic.TerminalBasic;
import online.nostrium.main.core;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.servers.terminal.notifications.ClientType;
import online.nostrium.servers.terminal.notifications.Session;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;
import online.nostrium.utils.screens.Screen;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * @author Brito
 * @date: 2024-09-17
 * @location: Germany
 */
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final String token;

    HashMap<Long, Session> sessions = new HashMap<>();

    public TelegramBot(String botToken) {
        token = botToken;
        telegramClient
                = new OkHttpTelegramClient(botToken);
    }

    @Override
    public void consume(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleCommand(update);
        }
    }

    /**
     * Handle the command request
     *
     * @param update
     * @return
     */
    private void handleCommand(Update update) {

        long chat_id = update.getMessage().getChatId();
        Screen screen = new ScreenTelegram(telegramClient, chat_id);

        Session session = getSession(update, screen);
        // don't reply to bots
        if (session == null) {
            return;
        }

        // get the text
        String text = update.getMessage().getText();

        long gap = 4 * 60 * 60 * 1000;
        long lastPingAccepted = System.currentTimeMillis() - gap;

        if (lastPingAccepted > session.getLastPing()) {
            // show the intro
            screen.writeIntro();
        }
        // Show that the session is still active
        session.ping();

        // Handle the command request
        CommandResponse response
                = session.getApp().handleCommand(
                        TerminalType.ANSI, text);

        // Ignore null responses
        if (response == null) {
            // Output the next prompt
            screen.writeUserPrompt(session.getApp());
            return;
        }

        // Is it time to go down one app?
        if (response.getCode() == TerminalCode.EXIT_APP
                && session.getApp().appParent != null) {
            session.setApp(session.getApp().appParent);
        }

        // Is it time to change apps?
        if (response.getCode() == TerminalCode.CHANGE_APP) {
            session.setApp(response.getApp());
            if (session.getApp().appParent != null) {
                screen.writeln(session.getApp().getIntro());
            }
        }

        // Output the reply
        if (response.getText().length() > 0) {
            // Output the message
            screen.writeln(response.getText());
        }

        // Output the next prompt
        screen.writeUserPrompt(session.getApp());

    }

    /**
     * Get the session for this specific user
     *
     * @param update
     * @return
     */
    private Session getSession(Update update, Screen screen) {

        Message message = update.getMessage();
        org.telegram.telegrambots.meta.api.objects.User userFromMessage = message.getFrom();
        if (userFromMessage == null) {
            return null;
        }

        // Get the user's Telegram ID (a unique identifier for the user)
        Long userId = userFromMessage.getId();
        Session session = null;
        if (sessions.containsKey(userId)) {
            session = sessions.get(userId);
        } else {
            User user = UserUtils.createUserAnonymous();
            TerminalApp app = new TerminalBasic(screen, user);
            session = new Session(ClientType.TELEGRAM, app, user);
            sessions.put(userId, session);
            core.sessions.addSession(session);
            // show the intro
            screen.writeIntro();
        }

        return session;
    }

}
