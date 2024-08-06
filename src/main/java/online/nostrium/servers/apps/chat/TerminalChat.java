/*
 * The app for chatting with other users
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.chat;

import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import static online.nostrium.servers.terminal.TerminalColor.BLUE;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.users.User;
import online.nostrium.utils.TextFunctions;

/**
 * @author Brito
 * @date: 2024-08-06
 * @location: Germany
 */
public class TerminalChat extends TerminalApp {

    public TerminalChat(TerminalType terminalType, User user) {
        super(terminalType, user);
        //addCommand(new CommandUserPassword(this));
        //addCommand(new CommandUserSave(this));
    }

    @Override
    public String getDescription() {
        return "Chat with users";
    }

    // setup the default answer
    @Override
    public TerminalCommand defaultCommand() {
        return new TerminalCommand(this) {

            @Override
            public String commandName() {
                return "default";
            }

            @Override
            public String oneLineDescription() {
                return "Default answer to any commands";
            }

            @Override
            public CommandResponse execute(TerminalType terminalType, String parameters) {
                return reply(TerminalCode.NOT_FOUND);
            }
        };
    }

    @Override
    public String getIntro() {
        
        String title = "Chat";
        String text = TextFunctions.getWindowFrame(title);
        
        String intro = 
                paint(BLUE,
                    text
                );
        
        return intro;
    }

    @Override
    public String getName() {
        return "chat";
    }

}
