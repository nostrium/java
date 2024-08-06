/*
 * Chat archive
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.utils.JsonTextFile;

/**
 * Author: Brito
 * Date: 2024-08-06
 * Location: Germany
 */
public class ChatArchive extends JsonTextFile{
    
    final File file;
    
    @SuppressWarnings("unchecked")
    @Expose
    ArrayList<ChatMessage> messages = new ArrayList();

    public ChatArchive(File file) {
        this.file = file;
    }
    
    public void addMessage(ChatMessage message){
        messages.add(message);
    }

    public ArrayList<ChatMessage> getMessages() {
        return messages;
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

    @Override
    public File getFile() {
        return file;
    }
}
