/*
 * Chat message
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 * 
 * This class represents a chat message in the Nostr network.
 * 
 * Fields:
 * - id: Unique identifier for the message (generated).
 * - pubkey: Public key of the author.
 * - createdAt: Timestamp indicating when the message was created.
 * - kind: Type of the message (e.g., text note, event).
 * - tags: Array of tags for categorizing and filtering messages.
 * - content: Main content of the message.
 * 
 * Usage:
 * - Create a new ChatMessage object with necessary fields.
 * - Generate a unique ID based on the message content.
 * - Export the message as a JSON string.
 */
package online.nostrium.servers.apps.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.users.User;

/**
 * Author: Brito
 * Date: 2024-08-06
 * Location: Germany
 */
public class ChatMessage {
    
    @Expose
    final String id;
    
    @Expose
    final String pubkey;
    
    @Expose
    final long createdAt;
    
    @Expose
    final int kind;
    
    @Expose
    final ArrayList<String[]> tags;
    
    @Expose
    final String content;

    public ChatMessage(String pubkey, int kind, String content, ArrayList<String[]> tags) {
        this.pubkey = pubkey;
        this.createdAt = System.currentTimeMillis() / 1000L; // current timestamp in seconds
        this.kind = kind;
        this.tags = tags;
        this.content = content;
        this.id = generateId();
    }
    
    public ChatMessage(String pubkey, int kind, String content) {
        this.pubkey = pubkey;
        this.createdAt = System.currentTimeMillis() / 1000L; // current timestamp in seconds
        this.kind = kind;
        this.tags = new ArrayList<>(); // Initialize tags to an empty list
        this.content = content;
        this.id = generateId();
    }
    
     public ChatMessage(User user, String content) {
        this.pubkey = user.getNpub();
        this.createdAt = System.currentTimeMillis() / 1000L; // current timestamp in seconds
        this.kind = 1;
        this.tags = new ArrayList<>(); // Initialize tags to an empty list
        this.content = content;
        this.id = generateId();
    }
    
    /**
     * Generates a unique ID for the message based on its content.
     *
     * @return The unique ID as a hexadecimal string
     */
    private String generateId() {
        try {
            // Convert fields to string and concatenate
            StringBuilder data = new StringBuilder();
            data.append(pubkey);
            data.append(createdAt);
            data.append(kind);
            for (String[] tag : tags) {
                for (String tagElement : tag) {
                    data.append(tagElement);
                }
            }
            data.append(content);

            // Compute SHA-256 hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.toString().getBytes(StandardCharsets.UTF_8));

            // Convert to hexadecimal format
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger(ChatMessage.class.getName()).log(Level.SEVERE, null, e);
            return null;
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
            Logger.getLogger(ChatMessage.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
}
