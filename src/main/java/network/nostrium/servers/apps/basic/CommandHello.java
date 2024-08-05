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
import network.nostrium.servers.terminal.TerminalType;
import static network.nostrium.servers.terminal.TerminalColor.BLUE;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class CommandHello extends TerminalCommand{

    public CommandHello(TerminalApp app) {
        super(app);
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        if(parameters.isEmpty()){
            return reply(404, "Please type a name");
        }
        
        return reply(200, "Hello, "
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
