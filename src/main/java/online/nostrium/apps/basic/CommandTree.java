/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.basic;

import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.session.Session;
import online.nostrium.session.maps.MapBox;
import online.nostrium.session.maps.MapType;

/**
 * @author Brito
 * @date: 2024-08-05
 * @location: Germany
 */
public class CommandTree extends TerminalCommand {

    public CommandTree(TerminalApp app, Session session) {
        super(app, session);
        this.requireSlash = false;
        // add an alternative command
        //this.commandsAlternative.add("chdir");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        String text = "";
       
        MapBox map = (MapBox) session.getCurrentLocation();
        
        // only change to folders or apps
        if(map.getType() != MapType.FOLDER
                && map.getType() != MapType.APP){
            return reply(TerminalCode.INVALID, "Not a folder nor app, cannot use this location");
        }
        
        String tree = map.getTree();
        return reply(TerminalCode.OK, tree);
    }

    @Override
    public String commandName() {
        return "tree";
    }

    @Override
    public String oneLineDescription() {
        return "Show a tree of items available";
    }

}
