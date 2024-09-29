/*
 * GPT chat box, completely offline and off-the-record
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.gpt;

import online.nostrium.user.User;
import online.nostrium.session.NotificationType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.utils.screens.Screen;

/**
 * @author Brito
 * @date: 2024-09-10
 * @location: Germany
 */
public class TerminalGPT extends TerminalApp {

    public TerminalGPT(Screen screenAssigned, User user) {
        super(screenAssigned, user);
//        addCommand(new CommandUserShow(this));
//        //addCommand(new CommandUserPassword(this));
//        //addCommand(new CommandUserSave(this));
//        addCommand(new CommandUserSet(this));
    }

    @Override
    public String getDescription() {
        return "Chat with an AI. Uncensored, offline,off-the-record";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        return reply(TerminalCode.NOT_FOUND);
    }

    @Override
    public String getIntro() {
        String title = "GPT";
        String text = screen.getWindowFrame(title);
        text += screen.breakLine();
        text += "Write a question or type 'help' to see the available commands";
        return text;
    }

    @Override
    public String getName() {
        return "gpt";
    }

    @Override
    public void receiveNotification(User userSender, NotificationType notificationType, Object object) {
        screen.writeln("Received a notification");
    }

}
