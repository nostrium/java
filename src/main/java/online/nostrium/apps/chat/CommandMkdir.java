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
 * @date: 2024-08-04
 * @location: Germany
 */
public class CommandMkdir extends TerminalCommand{

    public CommandMkdir(TerminalApp app, Session session) {
        super(app, session);
        this.requireSlash = false;
        // add an alternative command
        this.commandsAlternative.add("md");
        this.internalCommand = false;
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        
        if(parameters == null || parameters.isEmpty()){
            return reply(TerminalCode.INCOMPLETE, "Please add a folder name (without spaces)");
        }
        
        
        return reply(TerminalCode.OK, "Current server time: " + java.time.LocalDateTime.now());
    }

    @Override
    public String commandName() {
        return "mkdir";
    }
    
    @Override
    public String oneLineDescription() {
        return "Create a new chat room";
    }


}
