/*
 * NOSTR operations
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.nostr;

import online.nostrium.user.User;
import online.nostrium.main.core;
import online.nostrium.session.NotificationType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalUtils;
import online.nostrium.utils.screens.Screen;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class TerminalNostr extends TerminalApp {

    public TerminalNostr(Screen screenAssigned, User user) {
        super(screenAssigned, user);
//        addCommand(new CommandUserShow(this));
//        //addCommand(new CommandUserPassword(this));
//        //addCommand(new CommandUserSave(this));
//        addCommand(new CommandUserSet(this));
    }

    @Override
    public String getDescription() {
        return "Navigate NOSTR, write texts, use tools";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        
        
        return reply(TerminalCode.NOT_FOUND);
    }

    @Override
    public String getIntro() {
        String title = "NOSTR";
        String text = screen.getWindowFrame(title);
        text += screen.breakLine();
        text += "Type 'help' to see the available commands";
        return text;
    }

    @Override
    public String getName() {
        return "nostr";
    }

    @Override
    public void receiveNotification(User userSender, NotificationType notificationType, Object object) {
        screen.writeln("Received a notification");
    }

    @Override
    public String getId() {
        String path = TerminalUtils.getPath(this);
        return path;
    }

}
