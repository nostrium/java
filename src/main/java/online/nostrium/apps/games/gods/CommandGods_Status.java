/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.games.gods;

import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalColor;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.utils.AsciiArt;

/**
 * @author Brito
 * @date: 2024-08-21
 * @location: Germany
 */
public class CommandGods_Status extends TerminalCommand {

    public CommandGods_Status(TerminalGODS app) {
        super(app);
        this.requireSlash = false;
        // add an alternative command
        this.commandsAlternative.add("s");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        app.screen.clearScreen();
        String text = "";
        
        text += this.app.screen.getWindowFrame(TerminalColor.GREEN, "STATUS");
        
        
        text += "\n"
                //+ "username: " + app.user.getDisplayName()
                + "\n"
                + "Level: " + "1" 
                + "\n"
                + "Defense: " + "1" 
                + "\n"
                + "Attack: " + "1" 
                + "\n"
                + "Luck: " + "1" 
                + "\n"
                + "Armour: " + "1" 
                + "\n"
                + "Weapon: " + "1" 
                + "\n"
                + "Items: " + "[]" 
                + "\n"
                
                ;
        return reply(TerminalCode.OK, text);
    }

    @Override
    public String commandName() {
        return "status";
    }

    @Override
    public String oneLineDescription() {
        return "Show the user status";
    }

}
