/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.chat;

import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.Screen;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;

/**
 * @author Brito
 * @date: 2024-08-09
 * @location: Germany
 */
public class CommandChatClear extends TerminalCommand {

    public CommandChatClear(TerminalApp app) {
        super(app);
        this.requireSlash = false;
        // add an alternative command
        this.commandsAlternative.add("cls");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        return reply(TerminalCode.OK, Screen.clearScreen());
    }

    @Override
    public String commandName() {
        return "clear";
    }

    @Override
    public String oneLineDescription() {
        return "Clear the screen";
    }

}
