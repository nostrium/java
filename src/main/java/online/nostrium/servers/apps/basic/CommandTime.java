/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.basic;

import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class CommandTime extends TerminalCommand{

    public CommandTime(TerminalApp app) {
        super(app);
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        return reply(TerminalCode.OK, "Current server time: " + java.time.LocalDateTime.now());
    }

    @Override
    public String commandName() {
        return "time";
    }
    
    @Override
    public String oneLineDescription() {
        return "Output the current time";
    }


}
