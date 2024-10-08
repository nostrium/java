/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.basic;

import java.util.ArrayList;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import static online.nostrium.servers.terminal.TerminalColor.BLUE;
import static online.nostrium.servers.terminal.TerminalColor.GREEN;
import online.nostrium.servers.web.App;
import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class CommandHelp extends TerminalCommand {

    public CommandHelp(TerminalApp app, Session session) {
        super(app, session);
        this.commandsAlternative.add("?");
        this.requireSlash = false;
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        
        String text = session.getScreen().getWindowFrame("Help menu")
                + session.getScreen().breakLine()
                + session.getScreen().breakLine()
                ;
        
        // list all the apps first
        if (this.app.appChildren.isEmpty() == false) {
            text += "Available apps (type cd to enter them):";
            for (App appHere : this.app.appChildren) {
                
                // avoid listing non-permitted apps
                if(appHere.permissions.isPermitted(session.getUser()) == false){
                    continue;
                }
                
                String textName = "[" + appHere.getIdName() + "]";
                text += " "
                        + session.getScreen().breakLine()
                        + " "
                        + paint(GREEN, textName)
                        + ": "
                        + appHere.getDescription();
            }
            text += session.getScreen().breakLine() + session.getScreen().breakLine();
        }
        
        
        
        text += "Available commands:";

        @SuppressWarnings("unchecked")
        ArrayList<String> commandList = new ArrayList();

        
        
        // sort the ones with slash first
        for (TerminalCommand command : app.commands.values()) {
            if (command.requireSlash == false && command.internalCommand == false) {
                commandList.add(command.commandName());
            }
        }
        
        for (TerminalCommand command : app.commands.values()) {
            if (command.requireSlash && command.internalCommand == false) {
                commandList.add(command.commandName());
            }
        }
        
        // internal commands below
        
        for (TerminalCommand command : app.commands.values()) {
            if (command.requireSlash == false && command.internalCommand == true) {
                commandList.add(command.commandName());
            }
        }

        for (TerminalCommand command : app.commands.values()) {
            if (command.requireSlash && command.internalCommand == true) {
                commandList.add(command.commandName());
            }
        }
        
        
        
        
        // list all the commands
        boolean addDivider = false;
        for (String commandName : commandList) {
            if(commandName.equalsIgnoreCase("help")){
                continue;
            }
            TerminalCommand command = app.commands.get(commandName);
            if(command.internalCommand == false){
                addDivider = true;
            }
            String commandNames = command.getPathWithName();
            if (command.commandsAlternative.isEmpty() == false) {
                for (String commandToAdd : command.commandsAlternative) {
                    if (command.requireSlash) {
                        commandToAdd = "/" + commandToAdd;
                    }
                    commandNames += " | " + commandToAdd;
                }
            }

            
            // do we need to add a divider
            if(command.internalCommand && addDivider){
                text += "\n  "
                    + paint(GREEN, "---------");
                addDivider = false;
                
            }
            
            text += "\n  "
                    + paint(BLUE, commandNames)
                    + ": "
                    + command.oneLineDescription();
            
        }
        
        // list all the commands, start with internal commands
        for (String commandName : commandList) {
            if(this.internalCommand == true){
                continue;
            }
            TerminalCommand command = app.commands.get(commandName);
            String commandNames = command.getPathWithName();
            if (command.commandsAlternative.isEmpty() == false) {
                for (String commandToAdd : command.commandsAlternative) {
                    if (command.requireSlash) {
                        commandToAdd = "/" + commandToAdd;
                    }
                    commandNames += " | " + commandToAdd;
                }
            }

            text += "\n  "
                    + paint(BLUE, commandNames)
                    + ": "
                    + command.oneLineDescription();
        }
        

        if (app.commands.size() > 0) {
            text += "\n";
        }

        return reply(TerminalCode.OK, text);
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
