/*
 * Defines a section or in some cases, a folder
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.forum;

import com.google.gson.annotations.Expose;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Brito
 * @date: 2024-09-22
 * @location: Germany
 */
public class Group implements Comparable<Group>{
    
    // is there a parent associated?
    @Expose
    Group parent = null; // sometimes can belong to a subgroup
     
    @Expose
    Set<Topic> topics = new TreeSet<>();
    
    @Expose
    Set<Group> subgroups = new TreeSet<>();
    
    @Expose
    final String id;
    
    @Expose
    String 
    title, description;
    
    public Group(String id) {
        id = id.replace(" ", "").toLowerCase();
        this.id = id;
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

    public void addTopic(Topic topicToAdd) {
        for(Topic topic : topics){
            if(topic.matches(topicToAdd)){
                return;
            }
        }
        topics.add(topicToAdd);
    }

    public Group getParent() {
        return parent;
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public Set<Group> getSubgroups() {
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

}
