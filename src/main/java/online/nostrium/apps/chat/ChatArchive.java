/*
 * Chat archive
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 * Author: Brito
 * Date: 2024-08-06
 * Location: Germany
 */
public class ChatArchive {
    
    
    @SuppressWarnings("unchecked")
    @Expose
    ArrayList<ChatMessage> messages = new ArrayList();

    public ChatArchive() {
    }
    
    public void addMessage(ChatMessage message){
        messages.add(message);
    }

    public ArrayList<ChatMessage> getMessages() {
        return messages;
    }
    
     /**
     * Import a JSON into an object
     *
     * @param file
     * @return null if something went wrong
     */
    public static ChatArchive jsonImport(File file) {
        if (file.exists() == false
                || file.isDirectory()
                || file.length() == 0) {
            return null;
        }
        try {
            String text = FileUtils.readFileToString(file, "UTF-8");
            Gson gson = new Gson();
            ChatArchive item = gson.fromJson(text, ChatArchive.class);
            return item;
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Save this object as JSON to its file.
     * @param file
     */
    public void save(File file) {
        saveToFile(file);
    }

    /**
     * Save this object as JSON to the specified file.
     *
     * @param file the file where the JSON will be saved
     */
    @SuppressWarnings("deprecation")
    public void saveToFile(File file) {
        try {
            String data = jsonExport();
            if (data != null) {
                FileUtils.writeStringToFile(file, data);
                Logger.getLogger(ChatArchive.class.getName()).log(Level.INFO, "Saved file: {0}", file.getPath());
            } else {
                Logger.getLogger(ChatArchive.class.getName()).log(Level.SEVERE, "Failed to export JSON");
            }
        } catch (IOException ex) {
            Logger.getLogger(ChatArchive.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Export this object as a JSON string.
     *
     * @return JSON string or null if something went wrong
     */
    public String jsonExport() {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .setPrettyPrinting();
            Gson gson = gsonBuilder.create();
            return gson.toJson(this);
        } catch (Exception e) {
            Logger.getLogger(ChatArchive.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

}
