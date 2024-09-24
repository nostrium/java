/*
 * Extra data for messages
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.archive;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Brito
 * @date: 2024-09-22
 * @location: Germany
 */
public class MessageExtra {

    @Expose
    ArrayList<String> tags = new ArrayList<>();
    
    @Expose
    HashMap<String, String> webMentions = new HashMap<>();
    
    @Expose
    long countViews = 0;

    public long getCountViews() {
        return countViews;
    }

    public void setCountViews(long countViews) {
        this.countViews = countViews;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public HashMap<String, String> getWebMentions() {
        return webMentions;
    }
    
}
