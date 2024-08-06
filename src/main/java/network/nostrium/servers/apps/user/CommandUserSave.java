/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package network.nostrium.servers.apps.user;

import network.nostrium.servers.terminal.CommandResponse;
import network.nostrium.servers.terminal.TerminalApp;
import network.nostrium.servers.terminal.TerminalCommand;
import network.nostrium.servers.terminal.TerminalType;
import static network.nostrium.servers.terminal.TerminalColor.BLUE;
import static network.nostrium.servers.terminal.TerminalColor.RED;

/**
 * @author Brito
 * @date: 2024-08-05
 * @location: Germany
 */
public class CommandUserSave extends TerminalCommand{

    public CommandUserSave(TerminalApp app) {
        super(app);
        this.requireSlash = false;
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        // try to save the user
        if(app.user.save()){
            return reply(200, paint(BLUE, "Saved to disk"));
        }else{
            return reply(500, paint(RED, "Failed to save"));
        }
    }

    @Override
    public String commandName() {
        return "save";
    }
    
    @Override
    public String oneLineDescription() {
        return "Save this profile to disk";
    }


}