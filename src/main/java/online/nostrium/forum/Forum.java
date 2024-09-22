/*
 * Defines the start for an email box, forum or blog
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.forum;

import com.google.gson.annotations.Expose;
import java.util.Set;
import java.util.TreeSet;

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
public class Forum {
    
    @Expose
    final String id;      // small description without spaces and lower case
    
    @Expose
    String title;   // human readable title
    
    @Expose
    String description;  // one line description
    
    @Expose
    Set<Group> groups = new TreeSet<>();

    public Forum(String id) {
        id = id.replace(" ", "").toLowerCase();
        this.id = id;
    }
    
     // Override toString() method
    @Override
    public String toString() {
        return this.id;
    }

    public void addGroup(Group group) {
        for(Group groupExisting : groups){
            if(groupExisting.id.equals(group.id)){
                return;
            }
        }
        groups.add(group);
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

    public Set<Group> getGroups() {
        return groups;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
