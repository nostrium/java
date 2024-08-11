/*
 * Runs a telnet server
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.main;

import java.io.File;
import java.util.Date;
import online.nostrium.notifications.Sessions;
import online.nostrium.servers.ServerTelnet;
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
    public static Sessions sessions = new Sessions();
    
    // track the time that it is up
    public static Date uptime = new Date();
    
    
    public static void main(String[] args) {
        
        File folder = Folder.getFolderBase();
        
        String logo = AsciiArt.intro();
        
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
