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
import static online.nostrium.servers.terminal.TerminalColor.RED;

/**
 * @author Brito
 * @date: 2024-08-05
 * @location: Germany
 */
public class CommandUserShow extends TerminalCommand{

    public CommandUserShow(TerminalApp app) {
        super(app);
        this.requireSlash = false;
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        // try to save the user
        String output = "";
        
        output += "npub: " + this.app.user.getNpub() + "\n";
        output += "nsec: " + this.app.user.getNsec() + "\n";
        output += "name: " + noneWhenEmpty(this.app.user.username) + "\n";
        output += "type: " + noneWhenEmpty(this.app.user.userType.toString()) + "\n";
        output += "about: " + noneWhenEmpty(this.app.user.aboutMe) + "\n";
        output += "www: " + noneWhenEmpty(this.app.user.website) + "\n";
        output += "updated: " + noneWhenEmpty(this.app.user.lastLoginTime) + "\n";
        output += "register: " + noneWhenEmpty(this.app.user.registeredTime)
                //+ "\n"
                ;
        
        return reply(TerminalCode.OK, output);
    }

    @Override
    public String commandName() {
        return "show";
    }
    
    @Override
    public String oneLineDescription() {
        return "Show the account details";
    }

    private String noneWhenEmpty(String text) {
        if(text == null || text.isEmpty()){
            return "[none]";
        }else
            return text;
    }


}
