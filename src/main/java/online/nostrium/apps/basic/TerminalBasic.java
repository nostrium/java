/*
 * The root app
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.basic;

import online.nostrium.apps.admin.TerminalAdmin;
import online.nostrium.servers.terminal.notifications.NotificationType;
import online.nostrium.apps.chat.TerminalChat;
import online.nostrium.apps.email.TerminalEmail;
import online.nostrium.apps.games.TerminalGames;
import online.nostrium.apps.nostr.TerminalNostr;
import online.nostrium.user.TerminalUser;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.screens.Screen;
import online.nostrium.user.User;
import online.nostrium.servers.terminal.TerminalUtils;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class TerminalBasic extends TerminalApp {

    public TerminalBasic(Screen screen, User user) {
        super(screen, user);
        // add some specific commands
        addCommand(new CommandHello(this));
        addCommand(new CommandTime(this));
        addCommand(new CommandStatus(this));
        //addCommand(new CommandVanity(this));
        addCommand(new CommandLogin(this));
        addCommand(new CommandRegister(this));
        
        addCommand(new CommandAbout(this));
        
        // add apps inside
        addApp(new TerminalAdmin(screen, user));
        addApp(new TerminalUser(screen, user));
        addApp(new TerminalChat(screen, user));
        addApp(new TerminalGames(screen, user));
        addApp(new TerminalNostr(screen, user));
        addApp(new TerminalEmail(screen, user));
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
        
//        String intro = paint(GREEN, AsciiArt.intro())
//                + "\n\n"
//                + paint(BLUE,
//                "The NOSTR BBS! Type 'help' to list the commands."
//                        + "\n"
//                );
        screen.writeIntro();
        return "";
    }

    @Override
    public String getName() {
        return "basic";
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
