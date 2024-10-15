/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.email;

import online.nostrium.servers.email.EmailMessage;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-10-11
 * @location: Germany
 */
public class CommandEmailWrite extends TerminalCommand {

    public CommandEmailWrite(TerminalApp app, Session session) {
        super(app, session);
        this.requireSlash = false;
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        // syntax:
        // write
        
        TerminalEmail emailApp = (TerminalEmail) app;
    
        // start a new message to be written
        emailApp.message = new EmailMessage();
        
        session.getScreen().writeln("You are now writing an email.");
        session.getScreen().writeln("Available commands: RESET, STOP or SEND");
        session.getScreen().writeln("Please start by writing a title for the email:");
        
        return reply(TerminalCode.OK);

    }

    @Override
    public String commandName() {
        return "write";
    }

    @Override
    public String oneLineDescription() {
        return "Write an email";
    }

}
