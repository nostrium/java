/*
 * Defines a conversation
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.archive;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 * @author Brito
 * @date: 2024-09-22
 * @location: Germany
 */
public class Topic extends Message implements Comparable<Topic>{
  
    @Expose
    final String id;
    
    final static String nameStart = "topic-"; 
    final static String nameEnding = ".json";
    final File folder;

    public Topic(String id, String title, Message message, File folder) {
        // define the starting content of this topic
        super(message.timestamp, message.content);
        this.content = message.content;
        this.title = title;
        this.folder = folder;
        // define the unique id
        id = id.replace(" ", "").toLowerCase();
        this.id = id;
        save();
    }
    
    // Override toString() method to return the id
    @Override
    public String toString() {
        return this.id;
    }

    public String getId() {
        return id;
    }
    
    // Implement Comparable to compare objects based on their toString() method
    @Override
    public int compareTo(Topic other) {
        return other.toString().compareTo(this.toString());
    }

    public boolean matches(Topic topic) {
        return this.id.equals(topic.id);
    }
    
    public File getFile(){
        String filename = nameStart + id + nameEnding;
        return new File(folder, filename);
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

    private void save() {
        String text = jsonExport();
        File file = getFile();
        try {
            FileUtils.writeStringToFile(file, text, Charset.defaultCharset());
        } catch (IOException ex) {
            Logger.getLogger(Group.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
