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
import online.nostrium.session.maps.Map;
import online.nostrium.session.maps.MapType;

/**
 * @author Brito
 * @date: 2024-08-05
 * @location: Germany
 */
public class CommandCd extends TerminalCommand {

    public CommandCd(TerminalApp app, Session session) {
        super(app, session);
        this.requireSlash = false;
        // add an alternative command
        this.commandsAlternative.add("chdir");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        String text = "";
        
        // remove empty spaces from the edges
        parameters = parameters.trim();
        
        // no need to continue when there are no parameters
        if(parameters == null || parameters.isEmpty()){
            return reply(TerminalCode.INVALID, text);
        }
        
        // change to a new location
        Map map = session.getCurrentLocation().findPath(parameters);
        if(map == null){
            return reply(TerminalCode.NOT_FOUND, "Not found");
        }
        
        // only change to folders or apps
        if(map.getType() != MapType.FOLDER
                && map.getType() != MapType.APP){
            return reply(TerminalCode.INVALID, "Not a folder nor app, cannot change to that location");
        }
        
        if(map.getRelatedFolderOrFile() == null){
            map.setRelatedFolderOrFile(map.getAppRelated().getRelatedFolder());
        }
        
        // set the new location
        session.setCurrentLocation(map);
        
        
        return reply(TerminalCode.CHANGE_APP, "");
        
    }

    @Override
    public String commandName() {
        return "cd";
    }

    @Override
    public String oneLineDescription() {
        return "Change to another app";
    }

}
