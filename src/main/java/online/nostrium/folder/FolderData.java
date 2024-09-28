/*
 * Write data how a specific folder should be used
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.folder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 * @author Brito
 * @date: 2024-09-27
 * @location: Germany
 */
public class FolderData {

    @Expose
    String title;   // human readable title

    @Expose
    String description;  // one line description
    
    final File file;

    @Expose
    final FolderType type;
    
    
    
    public static FolderData importFile(File appFolder) {
        File file = new File(appFolder, FolderUtils.nameFolderData);
        return jsonImport(file);
    }

     /**
     * Import a JSON into an object
     *
     * @param file
     * @return null if something went wrong
     */
    public static FolderData jsonImport(File file) {
        if (file.exists() == false
                || file.isDirectory()
                || file.length() == 0) {
            return null;
        }
        try {
            String text = FileUtils.readFileToString(file, "UTF-8");
            Gson gson = new Gson();
            FolderData item = gson.fromJson(text, FolderData.class);
            return item;
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean doesNotExit(File appFolder) {
        File file = new File(appFolder, FolderUtils.nameFolderData);
        return file.exists();
    }
    
    public FolderData(File folder, FolderType type, boolean createFile) {
        this.type = type;
        file = new File(folder, FolderUtils.nameFolderData);
        if(createFile){
            save();
        }
    }
    
    /**
     * Save the text file to disk
     */
    public void save(){
        String text = jsonExport();
        try {
            FileUtils.writeStringToFile(file, text, Charset.defaultCharset());
        } catch (IOException ex) {
            Logger.getLogger(FolderData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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

    public FolderType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static final String filename = FolderUtils.nameFolderData; // data.json

    
}
