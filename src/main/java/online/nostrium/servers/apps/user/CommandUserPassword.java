/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.user;

import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import static online.nostrium.servers.terminal.TerminalColor.BLUE;
import online.nostrium.utils.TextFunctions;

/**
 * @author Brito
 * @date: 2024-08-05
 * @location: Germany
 */
public class CommandUserPassword extends TerminalCommand{

    public CommandUserPassword(TerminalApp app) {
        super(app);
        this.requireSlash = false;
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        
        if(parameters == null){
            return reply(TerminalCode.INCOMPLETE, "Please include a password");
        }

        // clean the password
        parameters = TextFunctions.sanitizePassword(parameters);
        
        if(parameters.isEmpty()){
            return reply(TerminalCode.INCOMPLETE, "Please include a password (minimum 8 characters)");
        }
        
        if(parameters.length() < 8){
            return reply(TerminalCode.FAIL, "Too short, needs at minimum 8 characters");
        }
        
        if(parameters.length() > 256){
            return reply(TerminalCode.FAIL, "Too long password");
        }
        
        // change the password and save to disk
        app.user.setPassword(parameters);
        
        
        return reply(200, ""
                + paint(BLUE, "Password set")
        );
    }

    @Override
    public String commandName() {
        return "password";
    }
    
    @Override
    public String oneLineDescription() {
        return "Save this profile to disk";
    }


}
