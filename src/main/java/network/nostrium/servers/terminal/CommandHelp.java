/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package network.nostrium.servers.terminal;

import java.util.ArrayList;
import static network.nostrium.servers.terminal.TerminalColor.BLUE;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class CommandHelp extends TerminalCommand{

    public CommandHelp(TerminalApp app) {
        super(app);
        this.commandsAlternative.add("?");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        String text = "Available commands: \n";
        
        ArrayList<String> commandList = new ArrayList();
        // sort the ones with slash first
        for(TerminalCommand command : app.commands.values()){
            if(command.requireSlash && command.internalCommand == false){
                commandList.add(command.commandName());
            }
        }
        
        for(TerminalCommand command : app.commands.values()){
            if(command.requireSlash && command.internalCommand == true){
                commandList.add(command.commandName());
            }
        }
        
        // after this, sort the ones without slash
        for(TerminalCommand command : app.commands.values()){
            if(command.requireSlash == false){
                commandList.add(command.commandName());
            }
        }
        
        
        // list all the commands
        for(String commandName : commandList){
            TerminalCommand command = app.commands.get(commandName);
            String commandNames = command.getName();
            if(command.commandsAlternative.isEmpty() == false){
                for(String commandToAdd : command.commandsAlternative){
                    if(command.requireSlash){
                        commandToAdd = "/" + commandToAdd;
                    }
                    commandNames += " | " + commandToAdd;
                }
            }
            
            
            text += "\n   " 
                    + paint(BLUE, commandNames)
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
