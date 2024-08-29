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
import online.nostrium.servers.terminal.TerminalColor;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;

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
    
    private String show(String tag, String value) {
    return 
            app.screen.paint(TerminalColor.GREEN, tag)
            + ": " + value + "\n";
}

@Override
public CommandResponse execute(TerminalType terminalType, String parameters) {
    // try to save the user
    String output = "";
    
    output += show("npub", this.app.user.getNpub());
    output += show("nsec", this.app.user.getNsec());
    output += show("username", noneWhenEmpty(this.app.user.username));
    output += show("password hash", noneWhenEmpty(this.app.user.passwordHash));
    output += show("type", noneWhenEmpty(this.app.user.userType.toString()));
    output += show("email", noneWhenEmpty(this.app.user.getEmailAddress()));
    output += show("about", noneWhenEmpty(this.app.user.aboutMe));
    output += show("www", noneWhenEmpty(this.app.user.website));
    output += show("updated", noneWhenEmpty(this.app.user.lastLoginTime));
    output += show("registered", noneWhenEmpty(this.app.user.registeredTime));
    
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
