/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package network.nostrium.servers.apps.basic;

import network.nostrium.servers.terminal.CommandResponse;
import network.nostrium.servers.terminal.TerminalApp;
import network.nostrium.servers.terminal.TerminalCommand;
import network.nostrium.servers.terminal.TerminalType;

/**
 * @author Brito
 * @date: 2024-08-05
 * @location: Germany
 */
public class CommandLs extends TerminalCommand{

    public CommandLs(TerminalApp app) {
        super(app);
        this.requireSlash = false;
        // add an alternative command
        this.commandsAlternative.add("dir");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        return reply(200, "Current server time: " + java.time.LocalDateTime.now());
    }

    @Override
    public String commandName() {
        return "ls";
    }
    
    @Override
    public String oneLineDescription() {
        return "List the available items";
    }


}
