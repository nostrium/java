/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.basic;

import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import static online.nostrium.servers.terminal.TerminalColor.BLUE;
import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class CommandHello extends TerminalCommand{

    public CommandHello(TerminalApp app, Session session) {
        super(app, session);
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        if(parameters.isEmpty()){
            return reply(TerminalCode.NOT_FOUND, "Please type a name");
        }
        
        return reply(TerminalCode.OK, "Hello, "
                + paint(BLUE, parameters)
                + "!");
    }

    @Override
    public String commandName() {
        return "hello";
    }
    
    @Override
    public String oneLineDescription() {
        return "Say hello to something";
    }


}
