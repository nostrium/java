/*
 *  Basic config for the forum
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package network.nostrium.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.util.ArrayList;
import network.nostrium.forum.structures.AffiliateLink;
import network.nostrium.forum.structures.URLtype;
import static network.nostrium.forum.structures.URLtype.IPB;
import org.apache.commons.io.FileUtils;

/**
 * Date: 2023-02-08
 * Place: Germany
 * @author brito
 */
public class Config {

    // variables start with upper case to improve 
    // JSON readability when exported/imported
    
    String ForumTitle = "";
    String ForumDescription = "";
    String ForumTheme = "default";
    String FoundationDate = "";
    String ForumAdminPubKey = "";
    ArrayList<String> ForumModeratorsPubKeys = new ArrayList();
    ArrayList<AffiliateLink> Links = new ArrayList();
    
    // options
    boolean EnableDownloadForumAsZip = true; // download the whole forum data
    boolean EnableViewStatsForGuests = true;    // let users view usage stats
    boolean EnableUploadFiles = true;
    boolean EnableDownloadConversationAsStaticHTML = true;
    boolean EnableUserSignatures = true;
    boolean EnableAutoUpdate = true;        // auto-upgrade to new versions
    boolean EnableReadOnly = false;         // don't permit changes on forum data
    boolean EnableAccountClaim = true;      // after porting, permit old users recover access
    int hoursToDeleteNonConfirmedNote = 48; // to reduce spammers
    boolean EnableNonLoggedVisitorsToRead = true; // non-logged visitors can read posts
//    Level[] Levels = new Level[]{ // define levels for user group permissions
//        new Level1(),
//        new Level2(),
//        new Level3()
//    }; 
    URLtype URLtype = IPB;
    
    
    
    /**
     * Export this object as JSON
     *
     * @return null if something went wrong
     */
    public String jsonExport() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                //.excludeFieldsWithoutExposeAnnotation()
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

}
