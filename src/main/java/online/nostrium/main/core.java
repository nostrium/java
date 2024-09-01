/*
 * Launch the servers
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.main;

import java.io.File;
import java.util.Date;
import online.nostrium.logs.Log;
import online.nostrium.nostr.nip05.NIP05;
import online.nostrium.nostr.nip05.NIP05_emails;
import online.nostrium.servers.terminal.notifications.Sessions;
import online.nostrium.servers.Server;
import online.nostrium.servers.email.ServerEmail;
import online.nostrium.servers.finger.ServerFinger;
import online.nostrium.servers.qoft.ServerQOTD;
import online.nostrium.servers.telnet.ServerTelnet;
import online.nostrium.servers.terminal.TerminalCode;
import static online.nostrium.servers.terminal.TerminalCode.BOOT;
import online.nostrium.servers.web.ServerWeb;
import online.nostrium.translation.LanguageControl;
import online.nostrium.utils.AsciiArt;
import online.nostrium.utils.time;

/**
 * @author Brito
 * @date: 2024-08-06
 * @location: Germany
 */
public class core {

    public static Config config;
    public static Sessions sessions = new Sessions();
    public static LanguageControl lang = new LanguageControl();

    // track the time that it is up
    public static Date uptime = new Date();

    public static boolean keepRunning = true;
    
    
    public static void runServers(){
        Server[] servers = new Server[]{
            new ServerTelnet(),
            new ServerWeb(),
            new ServerFinger(),
            //new ServerSSH(),
            new ServerQOTD(),
            new ServerEmail()
        };

        for (Server server : servers) {
            server.start();
            // don't keep running when a problem happened
            if (server.isRunning() == false) {
                keepRunning = false;
            }
        }
        
        if(keepRunning){
            //System.out.println("Nostrium is now running");
            Log.write(TerminalCode.OK,"Nostrium is now running");
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
        Log.write(TerminalCode.OK,"Stopped the servers");
    }

    
    public static void main(String[] args) {

        File folder = Folder.getFolderBase();

        String logo = AsciiArt.intro();

        System.out.println(logo);
        System.out.println("");
        System.out.println(AsciiArt.description());
        System.out.println("--------------------------");
        Log.write(BOOT, "Data folder", folder.getPath());

        // get the config started
        config = Config.loadConfig();
        
        // run the scheduled tasks
        NIP05_emails nip05 = new NIP05_emails();  // every 10 minutes
    
        

        // run servers and keep waiting
        runServers();
    }


}
