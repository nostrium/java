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
import online.nostrium.servers.terminal.TerminalColor;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-08-05
 * @location: Germany
 */
public class CommandUserShow extends TerminalCommand{

    public CommandUserShow(TerminalApp app, Session session) {
        super(app, session);
        this.requireSlash = false;
    }
    
    private String show(String tag, String value) {
    return 
            session.getScreen().paint(TerminalColor.GREEN, tag)
            + ": " + value + "\n";
}

@Override
public CommandResponse execute(TerminalType terminalType, String parameters) {
    // try to save the user
    String output = "";
    
    output += show("npub", session.getUser().getNpub());
    output += show("nsec", session.getUser().getNsec());
    output += show("username", noneWhenEmpty(session.getUser().username));
    output += show("password hash", noneWhenEmpty(session.getUser().passwordHash));
    output += show("type", noneWhenEmpty(session.getUser().userType.toString()));
    output += show("email", noneWhenEmpty(session.getUser().getEmailAddress()));
    output += show("about", noneWhenEmpty(session.getUser().aboutMe));
    output += show("www", noneWhenEmpty(session.getUser().website));
    output += show("updated", noneWhenEmpty(session.getUser().lastLoginTime));
    output += show("registered", noneWhenEmpty(session.getUser().registeredTime));
    
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
