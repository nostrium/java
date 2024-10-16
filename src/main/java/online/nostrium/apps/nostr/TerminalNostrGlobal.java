/*
 * NOSTR operations
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.nostr;

import online.nostrium.user.User;
import online.nostrium.session.NotificationType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class TerminalNostrGlobal extends TerminalApp {

    public TerminalNostrGlobal(Session session) {
        super(session);
        
//        addCommand(new CommandUserShow(this));
//        //addCommand(new CommandUserPassword(this));
//        //addCommand(new CommandUserSave(this));
//        addCommand(new CommandUserSet(this));
    }

    @Override
    public String getDescription() {
        return "Global texts from different relays";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        session.getScreen().writeln("I'm here");
        return reply(TerminalCode.NOT_FOUND);
    }

    @Override
    public String getIntro() {
        String title = "Global texts";
        String text = session.getScreen().getWindowFrame(title);
        text += session.getScreen().breakLine();
        text += "Type 'help' to see the available commands";
        return text;
    }

    @Override
    public String getIdName() {
        return "global";
    }

    @Override
    public void receiveNotification(User userSender, NotificationType notificationType, Object object) {
        session.getScreen().writeln("Received a notification");
    }

    @Override
    public String getPathVirtual() {
        return session.getCurrentLocation().getPath();
    }

}
