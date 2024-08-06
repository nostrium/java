/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package network.nostrium.servers.apps.basic;

import network.nostrium.servers.terminal.CommandResponse;
import network.nostrium.servers.terminal.TerminalApp;
import network.nostrium.servers.terminal.TerminalCommand;
import network.nostrium.servers.terminal.TerminalCode;
import network.nostrium.servers.terminal.TerminalType;
import network.nostrium.servers.terminal.TerminalUtils;

/**
 * @author Brito
 * @date: 2024-08-05
 * @location: Germany
 */
public class CommandCd extends TerminalCommand {

    public CommandCd(TerminalApp app) {
        super(app);
        this.requireSlash = false;
        // add an alternative command
        this.commandsAlternative.add("chdir");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        String text = "";
        
        // no need to continue when there are no parameters
        if(parameters == null || parameters.isEmpty()){
            return reply(TerminalCode.OK, text);
        }
        
        // shall we go down one directory?
        if(parameters.equalsIgnoreCase("..")){
            return reply(TerminalCode.EXIT_APP, "");
        }
        
        // move to the root directory
        if(parameters.equalsIgnoreCase("/")){
            TerminalApp appRoot = TerminalUtils.getAppRoot(this.app);
            return reply(appRoot);
        }
        
        
        // no need to continue when there is nothing
        if (this.app.appChildren.isEmpty()) {
            return reply(TerminalCode.OK, text);
        }

        // iterate all apps        
        for (TerminalApp appChild : this.app.appChildren) {
            String textName = appChild.getName();
            if(textName.equalsIgnoreCase(parameters)){
                return reply(appChild);
            }
        }
        
        return reply(200, "Not found");
    }

    @Override
    public String commandName() {
        return "cd";
    }

    @Override
    public String oneLineDescription() {
        return "Change directory/app";
    }

}
