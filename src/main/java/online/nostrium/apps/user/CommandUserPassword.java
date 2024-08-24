/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.user;

import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import static online.nostrium.servers.terminal.TerminalColor.GREEN;
import online.nostrium.utils.TextFunctions;
import static online.nostrium.utils.TextFunctions.sha256;

/**
 * @author Brito
 * @date: 2024-08-05
 * @location: Germany
 */
public class CommandUserPassword extends TerminalCommand{

    int minCharacters = 4;
    
    public CommandUserPassword(TerminalApp app) {
        super(app);
        this.requireSlash = false;
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        
        if(this.app.user.username == null){
            return reply(TerminalCode.INCOMPLETE, "Please define your username before setting a password");
        }
        
        if(parameters == null){
            return reply(TerminalCode.INCOMPLETE, "Please include a password");
        }

        // clean the password
        parameters = TextFunctions.sanitizePassword(parameters);
        
        if(parameters.isEmpty()){
            return reply(TerminalCode.INCOMPLETE, "Please include a password (minimum "
                    + minCharacters
                    + " characters)");
        }
        
        if(parameters.length() < 8){
            return reply(TerminalCode.FAIL, "Too short, needs at minimum "
                    + minCharacters
                    + " characters");
        }
        
        if(parameters.length() > 256){
            return reply(TerminalCode.FAIL, "Too long password");
        }
        
        // never store the password, just its hashed version
        String passwordHash = sha256(parameters);
        
        // change the password and save to disk
        app.user.setPasswordHash(passwordHash);
        
        
        return reply(TerminalCode.OK, ""
                + paint(GREEN, "Password set")
        );
    }

    @Override
    public String commandName() {
        return "password";
    }
    
    @Override
    public String oneLineDescription() {
        return "Set the password";
    }


}
