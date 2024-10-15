/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.basic;

import java.io.File;
import java.util.TreeSet;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalColor;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.session.Session;
import online.nostrium.session.maps.Map;
import online.nostrium.session.maps.MapApp;
import online.nostrium.session.maps.MapBox;
import online.nostrium.session.maps.MapFile;
import online.nostrium.session.maps.MapFolder;
import online.nostrium.utils.TextFunctions;

/**
 * @author Brito
 * @date: 2024-10-09
 * @location: Germany
 */
public class CommandLL extends TerminalCommand {

    public CommandLL(TerminalApp app, Session session) {
        super(app, session);
        this.requireSlash = false;
        // add an alternative command
        //this.commandsAlternative.add("");
    }

    @Override
    @SuppressWarnings("null")
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        
        TreeSet<Map> list = session.getCurrentLocation().listFiles(parameters);

        String text = "";
        int valTime = 20;
        int valSize = 10;
        
        
        int maxSizeName = 10;
        int maxSizeAppType = 3;
        for(Map map: list){
            if(map.getName().length() > maxSizeName){
                maxSizeName = map.getName().length();
            }
            if(map.getType().toString().length() > maxSizeAppType){
                maxSizeAppType = map.getType().toString().length();
            }
        }
        String gap = TextFunctions.createLineWithText(maxSizeName, " ");
        
        
        
        String  dataName = gap,
                dataType = gap, 
                dataTime = gap, 
                dataSize = gap;

        for (Map map : list) {
            // default color is green
            TerminalColor color = TerminalColor.GREEN;
            
            // is this an app or folder?
            if(map instanceof MapApp || map instanceof MapFolder){
                color = TerminalColor.CYAN;
                long count = countItems(map);
                if(count == 1){
                    dataTime = "(1 item)";
                }else{
                    dataTime = "("
                            + count
                            + " items)";
                }
            }
            
            // are we handling a file?
            if(map instanceof MapFile){
                MapFile mapFile = (MapFile) map;
                File file = mapFile.getRelatedFolderOrFile();
                
                // setup the time of the file
                dataTime = TextFunctions
                        .convertLongToDateTimeWithSeconds(
                                file.lastModified()
                        ) + gap;
                dataTime = dataTime.substring(0, valTime);
                
                // display the size of the file
                dataSize = TextFunctions.humanReadableFileSize(file)
                        + gap;
                dataSize = dataSize.substring(0, valSize);
            }
            
            // setup the item type
            dataType = "[" + map.getType().toString() + "]" + gap;
            dataType = dataType.substring(0, maxSizeAppType + 2);
            
            // setup the item name
            dataName = map.getName() + gap;
            dataName = dataName.substring(0, maxSizeName);
                    
            
            text +=   dataType 
                    + " " 
                    + paint(color, dataName)
                    + " "
                    + dataTime 
                    + " " 
                    + dataSize
                    + "\n";
        }
        
        // remove the last new line character
        if(text.isEmpty() == false){
            text = text.substring(0, text.length()-1);
        }

        return reply(TerminalCode.OK, text);
    }

    @Override
    public String commandName() {
        return "ll";
    }

    @Override
    public String oneLineDescription() {
        return "List available items with details";
    }

    private long countItems(Map map) {
        int count = 0;
        if(map instanceof MapBox){
            MapBox mapApp = (MapBox) map;
            count += mapApp.getFiles().size();
            count += mapApp.getLinks().size();
            for(MapApp app1 : mapApp.getApps()){
                count++;
                count += countItems(app1);
            }
            for(MapFolder app1 : mapApp.getFolders()){
                count++;
                count += countItems(app1);
            }
        }
        
        return count;       
    }

}
