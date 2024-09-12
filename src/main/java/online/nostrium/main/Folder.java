/*
 *  Provide the starting folder for the forum
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.main;

import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 * Date: 2023-02-08
 * Place: Germany
 * @author brito
 */
public class Folder {

    @Expose
    public static String 
            nameFileConfig = "config.json",
            nameFolderData = "data",
            nameFolderWWW = "www",
            nameFolderGPT = "gpt",
            nameFolderChat = "chat",
            nameFolderStories = "stories",
            nameFolderChatRoom = "room.json",
            nameFolderBase = "forum",
            nameFolderUsers = "users",
            nameFolderLang = "lang",
            nameFolderProfileImages = "profile_images",
            nameFolderLogs = "logs",
            nameRootChat = "root",
            nameAnonUsers = "Anon",
            nameSystem = "SYSTEM",
            nameEndingJsonUser = "-user.json",
            nameFileData = "data.json",
            namePermissions = "permissions";
    
    /**
     * There  are three type of paths that change: runtime, testing and custom.
     * Runtime is during development
     * Testing is during automated tests
     * Custom is when the user specifies a folder
     * @return 
     */
    public static File getFolderBase(){
        // check if we are in dev/test mode
        File folder = new File("./src");
        if(folder.exists() && folder.isDirectory()){
            return new File("./run/");
        }
        // return the present folder
        return new File(".");
    }
    
    public static File getFileConfig(){
        File folder = getFolderBase();
        File configFile = new File(folder, nameFileConfig);
        return configFile;
    }
    
    public static File getFolderTest(){
        return defaultGetFolder("test");
    }

    public static File getFolderBaseForum() {
        return defaultGetFolder(nameFolderBase);
    }
    
    public static File getFolderUsers() {
        return defaultGetFolder(nameFolderUsers);
    }
    
    public static File getFolderData() {
        return defaultGetFolder(nameFolderData);
    }
    
    public static File getFolderChat() {
        return defaultGetFolder(nameFolderChat);
    }
    
    
    public static File getFolderProfileImages() {
        return defaultGetFolder(getFolderData(), nameFolderProfileImages);
    }
    

    /**
     * Makes sure that the new folder exists
     * @param folderBase
     * @param nameFolder
     * @return 
     */
    public static File defaultGetFolder(File folderBase, String nameFolder){
        File folder = new File(folderBase, nameFolder);
        // create the folder when it does not exist
        if(folder.exists() == false){
            try {
                FileUtils.forceMkdir(folder);
            } catch (IOException ex) {
                Logger.getLogger(Folder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return folder;
    }
    
    private static File defaultGetFolder(String nameFolder){
        return defaultGetFolder(getFolderBase(), nameFolder);
    }
    
    
    public static void deleteTestFolder() {
        File folder = getFolderTest();
        try {
            FileUtils.deleteDirectory(folder);
        } catch (IOException ex) {
            Logger.getLogger(Folder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String makeWebCompatible(File file) {
        File folder = getFolderBase();
        String basePath = folder.getPath();
        String filepath = file.getPath().substring(basePath.length());
        return filepath;
    }

    public static File getFolderWWW() {
        return defaultGetFolder(nameFolderWWW);
    }

    public static File getFolderCerts() {
        return defaultGetFolder("certs");
    }

    public static File getFolderLang() {
        return defaultGetFolder(nameFolderLang);
    }

    public static File getFolderWellKnown() {
        return defaultGetFolder(getFolderWWW(), ".well-known");
    }

    public static File getFolderLog() {
        return defaultGetFolder(nameFolderLogs);
    }
    
    public static File getFolderStories() {
        return defaultGetFolder(nameFolderStories);
    }

    public static File getFolderGPT() {
         return defaultGetFolder(getFolderData(), nameFolderGPT);
    }
    
    
}
