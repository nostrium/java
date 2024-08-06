/*
 * The chat room
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.chat;

import com.google.gson.annotations.Expose;
import java.io.File;
import java.util.ArrayList;
import online.nostrium.main.Folder;
import online.nostrium.utils.JsonTextFile;


/**
 * @author Brito
 * @date: 2024-08-06
 * @location: Germany
 */
public class ChatRoom extends JsonTextFile{
    
    @Expose
    final String id;
    
    @Expose
    String name,            // one name without spaces
           description,     // one-line description
           password;        // define a password to enter (optional)
    
    @Expose
    boolean showHistory = true,
            needPasswordToEnter = false;
    
    
    @SuppressWarnings("unchecked")
    @Expose
    ArrayList<String> admins = new ArrayList();
    
    @SuppressWarnings("unchecked")
    @Expose
    ArrayList<String> mods = new ArrayList();     // the moderator team
    
    @SuppressWarnings("unchecked")
    @Expose
    ArrayList<String> tags = new ArrayList();     // tags defining this chat room
    
    @SuppressWarnings("unchecked")
    @Expose
    ArrayList<String> usersPermitted = new ArrayList();     // users permitted to talk
    
    @SuppressWarnings("unchecked")
    @Expose
    ArrayList<String> usersBlacklisted = new ArrayList();     // users permitted to talk
    
    
    
    

    public ChatRoom(String id) {
        this.id = id;
        
    }

    @Override
    public File getFile() {
        File folder = Folder.getFolderChat();
        
    }

}
