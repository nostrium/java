/*
 * Defines a message
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.archive;

import com.google.gson.annotations.Expose;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Brito
 * @date: 2024-09-22
 * @location: Germany
 */
public class Message implements Comparable<Topic>{
    
    // is there a parent associated?
    @Expose
    Message parent = null;
    
    @Expose
    Set<Message> replies = new TreeSet<>();
    
    @Expose
    final String timestamp;
    
    @Expose
    String title, description, authorNPUB;
    
    @Expose
    protected String content;
    
    @Expose
    public MessageExtra extra = new MessageExtra();
    
    
     // Implement Comparable to compare objects based on their toString() method
    @Override
    public int compareTo(Topic other) {
        return other.toString().compareTo(this.toString());
    }
    
    // Override toString() method to return the id
    @Override
    public String toString() {
        return this.timestamp;
    }

    public Message(String timestamp, String content) {
        this.timestamp = timestamp;
        this.content = content;
    }

    public Message getParent() {
        return parent;
    }

    public Set<Message> getReplies() {
        return replies;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthorNPUB() {
        return authorNPUB;
    }

    public String getContent() {
        return content;
    }

}
