/*
 *  Basic config for the forum
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.main;

import online.nostrium.folder.FolderUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.logs.Log;
import online.nostrium.servers.ports.DeployType;
import static online.nostrium.servers.terminal.TerminalCode.INVALID;
import static online.nostrium.servers.terminal.TerminalCode.OK;
import online.nostrium.servers.terminal.TerminalColor;
import online.nostrium.utils.JarUtils;
import org.apache.commons.io.FileUtils;

/**
 * @Date: 2023-02-08
 * @Place: Germany
 * @author brito
 */
public class Config {

    // variables start with upper case to improve 
    // JSON readability when exported/imported
    @Expose
    public boolean debug = true;

    @Expose
    public String domain_production = "nostrium.online",
            domain_debug = "127.0.0.1",
            language = "EN";

    @Expose
    public String tokenTelegram = "XXXXX",
            tokenTelegram_Debug = "YYYYY";

    @Expose
    public TerminalColor colorAppsDefault
            = TerminalColor.GREEN_BRIGHT;

    @Expose
    public boolean useSSL = true,
            enableServerTelnet = true,
            enableServerWWW = true,
            enableServerSSH = true,
            enableBotTelegramBot = true,
            enableBotSimpleX = true,
            enableServerEmail = true,
            enableServerIRC = true,
            enableServerFTP = true,
            enableServerFinger = true;

    @Expose
    public int portTelnet = 23,
            portTelnet_Debug = 2300,
            portSSH = 22,
            portSSH_Debug = 2200,
            portHTTP = 80,
            portHTTP_Debug = 8000,
            portHTTPS = 443,
            portHTTPS_Debug = 44300,
            portGopher = 70,
            portGopher_Debug = 7000,
            portSMTP = 25,
            portSMTP_Debug = 2500,
            portSMTPS = 465,
            portSMTPS_Debug = 4650,
            portIMAP = 143,
            portIMAP_Debug = 14300,
            portIMAPS = 993,
            portIMAPS_Debug = 9930,
            portPOP3 = 110,
            portPOP3_Debug = 11000,
            portPOP3S = 995,
            portPOP3S_Debug = 9950,
            portQOTD = 17,
            portQOTD_Debug = 1700,
            portFinger = 79,
            portFinger_Debug = 7900,
            portFTP = 21,
            portFTP_Debug = 2100,
            portFTPS = 990,
            portFTPS_Debug = 9900;

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
            Config item = gson.fromJson(text, Config.class);
            return item;
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings({"FinallyDiscardsException", "deprecation"})
    public void save() {
        try {
            File file = FolderUtils.getFileConfig();
            String data = jsonExport();
            FileUtils.writeStringToFile(file, data, "UTF-8");
            Log.write(OK, "Created config file", file.getPath());
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
    }

    @SuppressWarnings({"FinallyDiscardsException", "deprecation"})
    private static Config createAndWriteNew() {
        Config config = new Config();
        try {
            // are we in dev or production mode?
            boolean prodMode = JarUtils.isRunningFromJar();
            // running in production, start without debug then
            if (prodMode) {
                config.debug = false;
            }

            File file = FolderUtils.getFileConfig();
            String data = config.jsonExport();
            FileUtils.writeStringToFile(file, data);
            Log.write(OK, "Created config file", file.getPath());
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return config;
        }
    }

    /**
     * Load the configuration from disk, when it does not exist then it will
     * create one.
     *
     * @return
     */
    public static Config loadConfig() {

        File file = FolderUtils.getFileConfig();

        // there is no file? Create one
        if (file.exists() == false) {
            return createAndWriteNew();
        }

        // the config file exists, let's load it.
        Config config = Config.jsonImport(file);
        if (config == null) {
            Log.write(INVALID, "Invalid config, using default values", file.getPath());
            return new Config();
        }
        // the config file is valid, use it
        return config;
    }

    public String getDomain() {
        if (debug) {
            return domain_debug;
        } else {
            return domain_production;
        }
    }

    /**
     * Debug is assumed as development environment,
     * whereas non-debug is considered production.
     * @return 
     */
    public DeployType getDeployType() {
        if (this.debug) {
            return DeployType.DEVELOPMENT;
        } else {
            return DeployType.PRODUCTION;
        }
    }
;

}
