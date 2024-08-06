/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package network.nostrium.servers.apps.basic;

import network.nostrium.servers.terminal.CommandResponse;
import network.nostrium.servers.terminal.TerminalApp;
import static network.nostrium.servers.terminal.TerminalColor.GREEN;
import network.nostrium.servers.terminal.TerminalCommand;
import network.nostrium.servers.terminal.TerminalType;

/**
 * @author Brito
 * @date: 2024-08-05
 * @location: Germany
 */
public class CommandLs extends TerminalCommand {

    public CommandLs(TerminalApp app) {
        super(app);
        this.requireSlash = false;
        // add an alternative command
        this.commandsAlternative.add("chdir");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {

        String text = "";
        // no need to continue when there is nothing
        if (this.app.appChildren.isEmpty()) {
            return reply(200, text);
        }

        // iterate all apps        
        for (TerminalApp app : this.app.appChildren) {
            String textName = app.getName() + "/";
            text += ""
                    + paint(GREEN, textName)
                    + "\t";
        }

        return reply(200, text);
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
