/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package network.nostrium.servers.apps.basic;

import network.nostrium.servers.terminal.CommandResponse;
import network.nostrium.servers.terminal.TerminalApp;
import static network.nostrium.servers.terminal.TerminalColor.BLUE;
import network.nostrium.servers.terminal.TerminalCommand;
import network.nostrium.servers.terminal.TerminalType;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class CommandExit extends TerminalCommand{

    public CommandExit(TerminalApp app) {
        super(app);
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        return reply(-1, paint(BLUE, "Goodbye!"));
    }

    @Override
    public String commandName() {
        return "exit";
    }

    @Override
    public String oneLineDescription() {
        return "Exits the app";
    }

}
