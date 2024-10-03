/*
 * The root app
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.basic;

import online.nostrium.apps.admin.TerminalAdmin;
import online.nostrium.session.NotificationType;
import online.nostrium.apps.chat.TerminalChat;
import online.nostrium.apps.email.TerminalEmail;
import online.nostrium.apps.games.TerminalGames;
import online.nostrium.apps.nostr.TerminalNostr;
import online.nostrium.user.TerminalUser;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.user.User;
import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class TerminalBasic extends TerminalApp {

    public TerminalBasic(Session session) {
        super(session);
        // add some specific commands
        addCommand(new CommandHello(this, session));
        addCommand(new CommandTime(this, session));
        addCommand(new CommandStatus(this, session));
        //addCommand(new CommandVanity(this));
        addCommand(new CommandLogin(this, session));
        addCommand(new CommandRegister(this, session));
        
        addCommand(new CommandAbout(this, session));
        
        // add apps inside
        addApp(new TerminalAdmin(session));
        addApp(new TerminalUser(session));
        addApp(new TerminalChat(session));
        addApp(new TerminalGames(session));
        addApp(new TerminalNostr(session));
        addApp(new TerminalEmail(session));
    }

    @Override
    public String getDescription() {
        return "The basic CLI app";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        return reply(TerminalCode.NOT_FOUND);
    }

    @Override
    public String getIntro() {
        session.getScreen().writeIntro();
        return "";
    }

    @Override
    public String getPathWithName() {
        return "basic";
    }

    @Override
    public void receiveNotification(User userSender, NotificationType notificationType, Object object) {
        session.getScreen().writeln("Received a notification");
    }
    
    @Override
    public String getId() {
        return session.getCurrentLocation().getPath();
    }

}
