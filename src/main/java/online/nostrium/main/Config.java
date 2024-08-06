/*
 *  Basic config for the forum
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.utils.Log;
import org.apache.commons.io.FileUtils;

/**
 * Date: 2023-02-08 Place: Germany
 *
 * @author brito
 */
public class Config {

    // variables start with upper case to improve 
    // JSON readability when exported/imported
    
    @Expose
    public boolean debug = true;
    
    @Expose
    public int portTelnet = 23,
            portTelnet_Debug = 23000,
            portSSH = 22,
            portSSH_Debug = 22000,
            portWeb = 80,
            portWeb_Debug = 8080;

//    String ForumTitle = "";
//    String ForumDescription = "";
//    String ForumTheme = "default";
//    String FoundationDate = "";
//    String ForumAdminPubKey = "";
//    ArrayList<String> ForumModeratorsPubKeys = new ArrayList();
//    ArrayList<AffiliateLink> Links = new ArrayList();
    // options
//    boolean EnableDownloadForumAsZip = true; // download the whole forum data
//    boolean EnableViewStatsForGuests = true;    // let users view usage stats
//    boolean EnableUploadFiles = true;
//    boolean EnableDownloadConversationAsStaticHTML = true;
//    boolean EnableUserSignatures = true;
//    boolean EnableAutoUpdate = true;        // auto-upgrade to new versions
//    boolean EnableReadOnly = false;         // don't permit changes on forum data
//    boolean EnableAccountClaim = true;      // after porting, permit old users recover access
//    int hoursToDeleteNonConfirmedNote = 48; // to reduce spammers
//    boolean EnableNonLoggedVisitorsToRead = true; // non-logged visitors can read posts
//    Level[] Levels = new Level[]{ // define levels for user group permissions
//        new Level1(),
//        new Level2(),
//        new Level3()
//    }; 
//    URLtype URLtype = IPB;
    /**
     * Export this object as JSON
     *
     * @return null if something went wrong
     */
    public String jsonExport() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                //.enableComplexMapKeySerialization()
                //.setLenient()
                .setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }

    /**
     * Import a JSON into an object
     *
     * @param file
     * @return null if something went wrong
     */
    public static Config jsonImport(File file) {
        if (file.exists() == false
                || file.isDirectory()
                || file.length() == 0) {
            return null;
        }
        try {
            String text = FileUtils.readFileToString(file, "UTF-8");
            Gson gson = new Gson();
            Config conf = gson.fromJson(text, Config.class);
            return conf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("FinallyDiscardsException")
    private static Config createAndWriteNew() {
        Config config = new Config();
        try {
            File file = Folder.getFileConfig();
            String data = config.jsonExport();
            FileUtils.writeStringToFile(file, data);
            Log.write("Created config file: " + file.getPath());
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return config;
        }
    }

    /**
     * Load the configuration from disk, when it does not exist then it will
     * create one.
     */
    public static Config loadConfig() {

        File file = Folder.getFileConfig();

        // there is no file? Create one
        if (file.exists() == false) {
            return createAndWriteNew();
        }

        // the config file exists, let's load it.
        Config config = Config.jsonImport(file);
        if (config == null) {
            Log.write("Invalid config, using default values: " + file.getPath());
            return new Config();
        }
        // the config file is valid, use it
        return config;
    }

}
