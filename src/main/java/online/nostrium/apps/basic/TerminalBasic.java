/*
 * The root app
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.basic;

import online.nostrium.notifications.NotificationType;
import online.nostrium.apps.chat.TerminalChat;
import online.nostrium.apps.games.TerminalGames;
import online.nostrium.apps.games.gods.TerminalGODS;
import online.nostrium.apps.user.TerminalUser;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import static online.nostrium.servers.terminal.TerminalColor.BLUE;
import static online.nostrium.servers.terminal.TerminalColor.GREEN;
import online.nostrium.servers.terminal.screens.Screen;
import online.nostrium.apps.user.User;
import online.nostrium.servers.terminal.TerminalUtils;
import online.nostrium.utils.AsciiArt;

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
        
        // add apps inside
        addApp(new TerminalUser(screen, user));
        addApp(new TerminalChat(screen, user));
        addApp(new TerminalGames(screen, user));
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
        
        String intro = paint(GREEN, AsciiArt.intro())
                + "\n\n"
                + paint(BLUE,
                "The NOSTR BBS! Type '/help' to list the commands."
                        + "\n"
                );
        
        return intro;
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
