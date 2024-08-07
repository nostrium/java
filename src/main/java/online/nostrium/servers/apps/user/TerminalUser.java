/*
 * The root app
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.user;

import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import static online.nostrium.servers.terminal.TerminalColor.BLUE;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.users.User;
import online.nostrium.utils.TextFunctions;

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
    }

    @Override
    public String getDescription() {
        return "User functions (e.g. login, save, edit)";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        return reply(TerminalCode.NOT_FOUND);
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
