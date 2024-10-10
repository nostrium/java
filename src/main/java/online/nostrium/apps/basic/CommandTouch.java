/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.basic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.session.Session;
import online.nostrium.session.maps.Map;
import online.nostrium.session.maps.MapBox;
import online.nostrium.session.maps.MapFile;
import org.apache.commons.io.FileUtils;

/**
 * @author Brito
 * @date: 2024-10-11
 * @location: Germany
 */
public class CommandTouch extends TerminalCommand {

    public CommandTouch(TerminalApp app, Session session) {
        super(app, session);
        this.requireSlash = false;
        // add an alternative command
        //this.commandsAlternative.add("");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        // syntax
        // touch filename.txt

        // get the base folder
        File folder = session.getCurrentLocation().getRelatedFolderOrFile();
        
        // create the filename
        String filename = parameters;
        File file = new File(folder, filename);

        if(file.exists()){
            return reply(TerminalCode.INVALID, "File already exists");
        }
        
        try {
            
            file.createNewFile();
            file.setLastModified(System.currentTimeMillis());
            
            // create the MapFile
            MapFile mapFile = new MapFile(file.getName());
            mapFile.setRelatedFolderOrFile(file);
            // add it to the map
            MapBox mapCurrent = (MapBox) session.getCurrentLocation();
            mapCurrent.addFile(mapFile);
            
        } catch (IOException ex) {
            Logger.getLogger(CommandTouch.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return reply(TerminalCode.OK, "File was created");

    }

    @Override
    public String commandName() {
        return "touch";
    }

    @Override
    public String oneLineDescription() {
        return "Create an empty file";
    }

}
