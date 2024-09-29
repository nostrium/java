/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.chat;

import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-08-09
 * @location: Germany
 */
public class CommandChatClear extends TerminalCommand {

    public CommandChatClear(TerminalApp app, Session session) {
        super(app, session);
        this.requireSlash = false;
        // add an alternative command
        this.commandsAlternative.add("cls");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        session.getScreen().clearScreen();
        return reply(TerminalCode.OK, "");
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
