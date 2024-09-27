/*
 * The JSON file export for the ./well-known/nostr.json file
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.nostr.nip05;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;
import online.nostrium.logs.Log;
import online.nostrium.folder.FolderUtils;
import online.nostrium.servers.terminal.TerminalCode;
import org.apache.commons.io.FileUtils;

/**
 * @author Brito
 * @date: 2024-09-01
 * @location: Germany
 */
public class NIP05 {
  // This map holds the user identifiers and their associated public keys
    private Map<String, String> names;

    public NIP05() {
        this.names = new HashMap<>();
    }

    public void addName(String identifier, String publicKey) {
        this.names.put(identifier, publicKey);
    }

    public Map<String, String> getNames() {
        return names;
    }

    public void setNames(Map<String, String> names) {
        this.names = names;
    }

    /**
     * Exports this NIP05 object as a JSON string.
     *
     * @return JSON representation of this NIP05 object.
     */
    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    
     /**
     * Generate the ./well-known/nostr.json file
     */
    public void save(){
        String filename = "nostr.json";
        File file = new File(FolderUtils.getFolderWellKnown(), filename);
        String text = this.toJson();
        try {
            FileUtils.writeStringToFile(file, text, "UTF-8");
        } catch (IOException ex) {
            Log.write("NIP05", TerminalCode.CRASH, 
                    "Failed to write nostr.json", 
                    file.getPath());
        }
    }

    public static void main(String[] args) {
        // Example usage
        NIP05 nip05 = new NIP05();
        nip05.addName("username", "npub1examplepublickeyhere...");
        nip05.addName("anotheruser", "npub1anotherexamplepublickey...");

        String json = nip05.toJson();
        System.out.println(json);
    }

    /**
     * Scan all valid users on the database and add them
     */
    public void scan() {
        // TODO: Avoid cases of banned usernames
        ArrayList<File> files = UserUtils.getUserFiles();
        for(File file : files){
            User user = User.jsonImport(file);
            if(user == null){
                return;
            }
            if(user.hasUsername() == false){
                continue;
            }
            this.addName(user.getUsername(), user.getNpub());
        }
    }
}