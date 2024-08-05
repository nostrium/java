/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package network.nostrium.servers.terminal;

import static network.nostrium.servers.terminal.TerminalColor.BLUE;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class CommandHelp extends TerminalCommand{

    public CommandHelp(TerminalApp app) {
        super(app);
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        String text = "Available commands: \n";
       
        for(TerminalCommand command : app.commands.values()){
            text += "\n   " 
                    + paint(BLUE, command.getName())
                    + ": "
                    + command.oneLineDescription();
        }
        
        if(app.commands.size() > 0){
            text += "\n";
        }
        
        return reply(200, text);
    }

    @Override
    public String commandName() {
        return "help";
    }

    @Override
    public String oneLineDescription() {
        return "This help menu";
    }

}
