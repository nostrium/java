/*
 * Defines the start for an email box, forum or blog
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.archive;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.logs.Log;
import online.nostrium.servers.terminal.TerminalCode;
import org.apache.commons.io.FileUtils;

/**
 * Make a structure that be used in parallel for:
 *  + forum
 *  + blog
 *  + HN/Reddit-style conversations
 *  + email
 *  + NNTP
 * 
 * @author Brito
 * @date: 2024-09-22
 * @location: Germany
 */
public class Archive {
    
    
    private final 
        String filename = "archive.json";
    
   private final File folder;     
        
    @Expose
    final String id;      // small description without spaces and lower case
    
    @Expose
    String title;   // human readable title
    
    @Expose
    String description;  // one line description
    
    public Archive(String id, File folder) {
        id = id.replace(" ", "").toLowerCase();
        this.id = id;
        this.folder = new File(folder, id);
        if(folder.exists() == false){
            try {
                FileUtils.forceMkdir(folder);
            } catch (IOException ex) {
                Logger.getLogger(Archive.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        save();
    }
    
     // Override toString() method
    @Override
    public String toString() {
        return this.id;
    }

    public void addGroup(Group group) {
        String idToAdd = group.getId();
        File folderGroup = new File(folder, idToAdd);
        if(folderGroup.exists() == false){
            try {
                FileUtils.forceMkdir(folderGroup);
            } catch (IOException ex) {
                Logger.getLogger(Archive.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        group.setFolder(folderGroup);
        // save the file inside
        group.save();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getGroups() {
        Set<String> list = new TreeSet<>();
        // list all files inside the folder
        File[] files = folder.listFiles();
        for(File file : files){
            if(file.isFile()){
                continue;
            }
            list.add(file.getName());
        }
        return list;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void save() {
        String text = this.jsonExport();
        File file = getFile();
        try {
            FileUtils.writeStringToFile(file, text, Charset.defaultCharset());
        } catch (IOException ex) {
            Log.write("ARCHIVE", TerminalCode.CRASH, "Write archive json", ex.getMessage());
        }
    }
    
    public File getFile(){
        return new File(folder, filename);
    }
}
