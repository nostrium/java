/*
 * Runs a telnet server
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.main;

import java.io.File;
import online.nostrium.servers.ServerTelnet;
import online.nostrium.servers.terminal.TerminalColor;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.servers.terminal.TerminalUtils;
import online.nostrium.utils.AsciiArt;
import online.nostrium.utils.Log;

/**
 *
 * To test the telnet server do this from the command line:
 *
 * telnet 127.0.0.1 10101
 *
 *
 * @author Brito
 * @date: 2024-08-06
 * @location: Germany
 */
public class core {

    public static Config config;
    
    
    public static void main(String[] args) {
        
        File folder = Folder.getFolderBase();
        File folderData = new File(folder, "data");
        
        String logo = TerminalUtils.paint(TerminalType.ANSI,
                TerminalColor.GREEN, AsciiArt.intro());
        
        
        System.out.println(logo);
        System.out.println("");
        System.out.println(AsciiArt.description());
        System.out.println("--------------------------");
        Log.write("Running from folder: " + folder.getPath());
        
        
        // get the config started
        config = Config.loadConfig();
        
        
        // telnet
        ServerTelnet.startServerTelnet();
        
    }

    

}
