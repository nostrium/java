/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.basic;

import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import static online.nostrium.servers.terminal.TerminalColor.GREEN;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-08-05
 * @location: Germany
 */
public class CommandLs extends TerminalCommand {

    public CommandLs(TerminalApp app, Session session) {
        super(app, session);
        this.requireSlash = false;
        // add an alternative command
        this.commandsAlternative.add("dir");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {

        String text = "";
        // no need to continue when there is nothing
        if (this.app.appChildren.isEmpty()) {
            return reply(TerminalCode.OK, text);
        }

        // iterate all apps        
        for (TerminalApp app : this.app.appChildren) {
            // don't list when permissions don't permit
            if(app.permissions.isPermitted(session.getUser()) == false){
                continue;
            }
            
            String textName = app.getPathWithName() + "/";
            text += ""
                    + paint(GREEN, textName)
                    + "\t";
        }

        return reply(TerminalCode.OK, text);
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
