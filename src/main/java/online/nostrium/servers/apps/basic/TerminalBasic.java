/*
 * The root app
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.basic;

import online.nostrium.servers.apps.chat.TerminalChat;
import online.nostrium.servers.apps.user.TerminalUser;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import static online.nostrium.servers.terminal.TerminalColor.BLUE;
import static online.nostrium.servers.terminal.TerminalColor.GREEN;
import online.nostrium.servers.terminal.screens.Screen;
import online.nostrium.users.User;
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
        
        // add apps inside
        addApp(new TerminalUser(screen, user));
        addApp(new TerminalChat(screen, user));
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

}
