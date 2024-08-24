/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.user;

import static online.nostrium.apps.user.UserUtils.setAbout;
import static online.nostrium.apps.user.UserUtils.setPassword;
import static online.nostrium.apps.user.UserUtils.setUsername;
import static online.nostrium.apps.user.UserUtils.setWWW;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;

/**
 * @author Brito
 * @date: 2024-08-05
 * @location: Germany
 */
public class CommandUserSet extends TerminalCommand{

    public CommandUserSet(TerminalApp app) {
        super(app);
        this.requireSlash = false;
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        
        if(parameters.length() == 0 || parameters.contains(" ") == false){
           return reply(TerminalCode.FAIL, "Parameters are invalid");
        }
        
        String[] value = parameters.split(" ");
        
        String action = value[0];
        String text = parameters.substring(action.length() + 1);
        
        // get the user
        User user = this.app.user;
        
        return switch (action) {
            case "username" -> setUsername(user, text);
            case "about" -> setAbout(user, text);
            case "www" -> setWWW(user, text);
            case "password" -> setPassword(user, text);
            default -> reply(TerminalCode.FAIL, "Invalid action");
        };
        
    }

    @Override
    public String commandName() {
        return "set";
    }
    
    @Override
    public String oneLineDescription() {
        return "Set a value (username | about | www)";
    }


}
