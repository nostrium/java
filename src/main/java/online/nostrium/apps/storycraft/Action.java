/*
 * Rule applicable to the game
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.storycraft;

import java.util.ArrayList;

/**
 * @author Brito
 * @date: 2024-09-06
 * @location: Germany
 */
public class Action {

    String action = null;
    String description = null;
    @SuppressWarnings("unchecked")
    ArrayList<String> rules = new ArrayList();

    public void addRule(String rule){
        rules.add(rule);
    }

    public ArrayList<String> getRules() {
        return rules;
    }
    
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
