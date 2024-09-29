/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.user;

import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import static online.nostrium.servers.terminal.TerminalColor.BLUE;
import static online.nostrium.servers.terminal.TerminalColor.RED;
import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-08-05
 * @location: Germany
 */
public class CommandUserSave extends TerminalCommand{

    public CommandUserSave(TerminalApp app, Session session) {
        super(app, session);
        this.requireSlash = false;
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        
        if(session.getUser().hasPassword() == false){
            return reply(TerminalCode.FAIL, "You need to first define a username and password");
        }
        
        // try to save the user
        if(session.getUser().save()){
            return reply(TerminalCode.OK, paint(BLUE, "Saved to disk"));
        }else{
            return reply(TerminalCode.FAIL, paint(RED, "Failed to save"));
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
