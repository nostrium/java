/*
 * The root app
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package network.nostrium.servers.apps.user;

import network.nostrium.servers.terminal.CommandResponse;
import network.nostrium.servers.terminal.TerminalApp;
import static network.nostrium.servers.terminal.TerminalColor.BLUE;
import network.nostrium.servers.terminal.TerminalCommand;
import network.nostrium.servers.terminal.TerminalType;
import network.nostrium.users.User;
import network.nostrium.utils.TextFunctions;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class TerminalUser extends TerminalApp {

    public TerminalUser(TerminalType terminalType, User user) {
        super(terminalType, user);
        addCommand(new CommandUserPassword(this));
        addCommand(new CommandUserSave(this));
        //addCommand(new CommandTime(this));
    }

    @Override
    public String getDescription() {
        return "User functions (e.g. login, save, edit)";
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
                return reply(404, "");
            }
        };
    }

    @Override
    public String getIntro() {
        
        String title = "Manage your user profile";
        String text = TextFunctions.getWindowFrame(title);
        
        String intro = 
                paint(BLUE,
                    text
                );
        
        return intro;
    }

    @Override
    public String getName() {
        return "user";
    }

}
