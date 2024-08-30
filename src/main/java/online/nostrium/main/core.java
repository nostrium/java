/*
 * Launch the servers
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.main;

import java.io.File;
import java.util.Date;
import online.nostrium.servers.terminal.notifications.Sessions;
import online.nostrium.servers.Server;
import online.nostrium.servers.finger.ServerFinger;
import online.nostrium.servers.telnet.ServerTelnet;
import online.nostrium.servers.web.ServerWeb;
import online.nostrium.utils.AsciiArt;
import online.nostrium.utils.Log;
import online.nostrium.utils.time;

/**
 * @author Brito
 * @date: 2024-08-06
 * @location: Germany
 */
public class core {

    public static Config config;
    public static Sessions sessions = new Sessions();

    // track the time that it is up
    public static Date uptime = new Date();

    public static boolean keepRunning = true;
    
    public static void runServers(){
        Server[] servers = new Server[]{
            new ServerTelnet(),
            new ServerWeb(),
            new ServerFinger()
        };

        for (Server server : servers) {
            server.start();
            // don't keep running when a problem happened
            if (server.isRunning() == false) {
                keepRunning = false;
            }
        }
        
        if(keepRunning){
            System.out.println("Nostrium is now running");
        }
        
        while (keepRunning) {
            for (Server server : servers) {
                // don't keep running when a problem happened
                if (server.isRunning() == false) {
                    keepRunning = false;
                }
            }
            time.waitMs(500);
        }
        System.out.println("Stopped the servers");
    }

    
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
        

        // run servers and keep waiting
        runServers();
    }


}
