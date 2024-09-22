/*
 * Defines a conversation
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.forum;

import com.google.gson.annotations.Expose;

/**
 * @author Brito
 * @date: 2024-09-22
 * @location: Germany
 */
public class Topic extends Message implements Comparable<Topic>{
  
    @Expose
    final String id;

    public Topic(String id, String title, Message message) {
        // define the starting content of this topic
        super(message.timestamp, message.content);
        this.content = message.content;
        this.title = title;
        // define the unique id
        id = id.replace(" ", "").toLowerCase();
        this.id = id;
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

}
