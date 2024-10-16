/*
 * Launch the servers
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.main;

import online.nostrium.folder.FolderUtils;
import java.io.File;
import java.util.Date;
import online.nostrium.logs.Log;
import online.nostrium.nostr.nip05.NIP05_emails;
import online.nostrium.session.Sessions;
import online.nostrium.servers.Server;
import online.nostrium.servers.email.ServerEmail;
import online.nostrium.servers.finger.ServerFinger;
import online.nostrium.servers.ftp.ServerFTP;
import online.nostrium.servers.qoft.ServerQOTD;
import online.nostrium.servers.telegram.ServerTelegram;
import online.nostrium.servers.telnet.ServerTelnet;
import online.nostrium.servers.terminal.TerminalCode;
import static online.nostrium.servers.terminal.TerminalCode.BOOT;
import online.nostrium.servers.web.ServerWeb;
import online.nostrium.translation.LanguageControl;
import online.nostrium.utils.JarUtils;
import online.nostrium.utils.ascii.AsciiArt;
import online.nostrium.utils.events.Events;
import online.nostrium.utils.time;

/**
 * @author Brito
 * @date: 2024-08-06
 * @location: Germany
 */
public class core {

    public static Config config;
    public static Sessions sessions = new Sessions();
    public static Events events = new Events();
    public static LanguageControl lang = new LanguageControl();

    // track the time that it is up
    public static Date uptime = new Date();

    private static boolean keepRunning = true;
    
    
    public static void runServers(){
        Server[] servers = new Server[]{
            new ServerTelnet(),
            new ServerWeb(),
            new ServerFinger(),
            //new ServerSSH(),
            new ServerQOTD(),
            new ServerEmail(),
//            new ServerChatBot()
            new ServerTelegram(),
            new ServerFTP(),
            
        };

        for (Server server : servers) {
            server.start();
            // don't keep running when a problem happened
            if (server.isRunning() == false) {
                Log.write(TerminalCode.FAIL,"Server stopped", 
                        server.getId() + ":" + server.getPorts().getInfo());
                keepRunning = false;
            }
        }
        
        if(keepRunning){
            Log.write(TerminalCode.OK,"Nostrium is now running");
        }
        
        while (keepRunning) {
            for (Server server : servers) {
                // don't keep running when a problem happened
                if (server.isRunning() == false) {
                    keepRunning = false;
                    Log.write(TerminalCode.FAIL,"Stopped server", server.getId());
                }
            }
            time.waitMs(500);
        }
        Log.write(TerminalCode.WARNING,"Server launch had troubles");
    }

    
    public static void startConfig(){
        // get the config started
        config = Config.loadConfig();
        // save new settings to file
        config.save();
        
        extractFilesIfNeeded();
    }
    
    public static void main(String[] args) {

        File folder = FolderUtils.getFolderBase();

        String logo = AsciiArt.intro();

        System.out.println(logo);
        System.out.println("");
        System.out.println(AsciiArt.description());
        System.out.println("--------------------------");
        Log.write(BOOT, "Data folder", folder.getPath());

        startConfig();
        
        // run the scheduled tasks
        runScheduledTasksAndActions();

        // run servers and keep waiting
        runServers();
    }

    private static void runScheduledTasksAndActions() {
        NIP05_emails nip05 = new NIP05_emails();  // every 10 minutes
    }

    /**
     * Extracts the files inside resources/extract to the root folder
     * when they are not yet existing.
     */
    private static void extractFilesIfNeeded() {
        File folderOutput = FolderUtils.getFolderBase();
        
        // update files in production mode
        if(JarUtils.isRunningFromJar()){
            String path = "extract";
            JarUtils.extractFolderFromJar(path, folderOutput);
        }
        
    }


}
