/*
 * Defines a section or in some cases, a folder
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.archive;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
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
 * @author Brito
 * @date: 2024-09-22
 * @location: Germany
 */
public class Group implements Comparable<Group>{
    
    public final String filename = "group.json";
    public File folder;
    
    
    @Expose
    final String id;
    
    @Expose
    String title, description;
    
    // is there a parent associated?
//    @Expose
//    Group parent = null; // sometimes can belong to a subgroup
//    
//    @Expose
//    Set<Group> subgroups = new TreeSet<>();
    
    public Group(String id, File folderParent) {
        id = id.replace(" ", "-").toLowerCase();
        folder = new File(folderParent, id);
        this.id = id;
    }
    
        public Group(File folder) {
        id = folder.getName();
        this.folder = folder;
    }
    
    // Override toString() method to return the id
    @Override
    public String toString() {
        return this.id;
    }
    
    // Implement Comparable to compare objects based on their toString() method
    @Override
    public int compareTo(Group other) {
        return this.toString().compareTo(other.toString());
    }

    public File getFolder() {
        return folder;
    }

    public void setFolder(File folder) {
        this.folder = folder;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    
    public void addTopic(Topic topicToAdd) {
//        for(String topic : topics){
//            if(topic.equals(topicToAdd.id)){
//                return;
//            }
//        }
//        topics.add(topicToAdd.id);
    }

//    public Group getParent() {
//        
//        return parent;
//    }

    public Set<String> getTopics() {
        Set<String> list = new TreeSet<>();
        // list all files inside the folder
        File[] files = folder.listFiles();
        for(File file : files){
            if(file.isDirectory()){
                continue;
            }
            if(file.getName().startsWith(Topic.nameStart)
                    && file.getName().endsWith(Topic.nameEnding)){
                list.add(file.getName());
            }
        }
        return list;
    }

    public Set<Group> getSubgroups() {
        Set<Group> subgroups = new TreeSet<>();
        for(File item : folder.listFiles()){
            if(item.isFile()){
                continue;
            }
            Group group;
            // is there a group json to be found?
            File file = new File(item, filename);
            if(file.exists()){
                group = Group.jsonImport(file);
                group.setFolder(item);
            }else{
                group = new Group(item);
            }
            subgroups.add(group);
            
        }
        return subgroups;
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
    public static Group jsonImport(File file) {
        if (file.exists() == false
                || file.isDirectory()
                || file.length() == 0) {
            return null;
        }
        try {
            String text = FileUtils.readFileToString(file, "UTF-8");
            Gson gson = new Gson();
            Group item = gson.fromJson(text, Group.class);
            return item;
        } catch (JsonSyntaxException | IOException e) {
            Log.write("FORUM-GROUP", TerminalCode.CRASH, "Failed to import json", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void save() {
        String text = this.jsonExport();
        File file = getFile();
        try {
            FileUtils.writeStringToFile(file, text, Charset.defaultCharset());
        } catch (IOException ex) {
            Logger.getLogger(Group.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public File getFile(){
        return new File(folder, filename);
    }

}
