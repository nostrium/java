/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.main;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.forum.structures.ManageUsers;
import online.nostrium.requests.RequestIndex;
import online.nostrium.utils.AsciiArt;
import online.nostrium.utils.Log;
import org.apache.commons.io.FileUtils;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.Exit;
import org.takes.http.FtBasic;
import org.takes.tk.TkFiles;

/**
 * Date: 2023-02-07
 * Place: Germany
 * @author brito
 */
public class coreOld {

    // use default settings
    public static Config config = new Config();
    public static ManageUsers users = new ManageUsers();
    
    
    public static void main(String[] args) {
        
        File folder = Folder.getFolderBase();
        File folderData = new File(folder, "data");
        
        System.out.println(AsciiArt.intro3());
        System.out.println(AsciiArt.description());
        System.out.println("--------------------------");
        Log.write("Running from folder: " + folder.getPath());
        
        // get the config started
        loadConfig();
        
        try {
            
            Log.write("Starting on port 8080");
           
            FtBasic basic = new FtBasic(
                    new TkFork(
                            new FkRegex("/data/.+", new TkFiles(folderData)),
                            new FkRegex(".+", new RequestIndex())
                    )
                    , 8080
            );

            basic.start(Exit.NEVER);
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(coreOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Load the configuration from disk,
     * when it does not exist then it will
     * create one.
     */
    private static void loadConfig() {
        File file = Folder.getFileConfig();
        
        // there is no file? Create one
        if(file.exists() == false){
            try {
                String data = coreOld.config.jsonExport();
                FileUtils.writeStringToFile(file, data);
            } catch (IOException ex) {
                Logger.getLogger(coreOld.class.getName()).log(Level.SEVERE, null, ex);
            }
            // nothing else to do, just exit
            Log.write("Created config file: " + file.getPath());
            return;
        }
        // the config file exists, let's load it.
        
        Config config = Config.jsonImport(file);
        if(config == null){
            try {
                String data = coreOld.config.jsonExport();
                FileUtils.writeStringToFile(file, data);
            } catch (IOException ex) {
                Logger.getLogger(coreOld.class.getName()).log(Level.SEVERE, null, ex);
            }
            // nothing else to do, just exit
            Log.write("Restored config file with default settings: " + file.getPath());
            return;
        }
        // the config file is valid, use it
        coreOld.config = config;
    }
    
    
}
