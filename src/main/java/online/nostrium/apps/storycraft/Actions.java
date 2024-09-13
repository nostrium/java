/*
 * Set of rules applicable to the game
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
public class Actions {

    final String keyword = "Action";
    final String anchor = "# "  + keyword + ": ";
    
    ArrayList<Action> list = new ArrayList<>();

    public void parse(String text) {
        if(text == null || text.isEmpty()){
            return;
        }
        
        // look for the specific blocks that we care
        ArrayList<String> blocks = StoryUtils.getTextBlocks(anchor, text);
        for(String block : blocks){
            Action action = new Action();
            // parse the action
            action.parse(block);
            // needs to be valid (title and script)
            if(action.isNotValid()){
                continue;
            }
            list.add(action);
        }
    }

     public String run(GameThing thingA, GameThing thingB, 
             String actionId, GameScreen screen) {
        // check that the action exists
        if (has(actionId) == false) {
            return null;
        }
        // run the action
        Action action = get(actionId);
        return action.processAction(thingA, thingB, screen);
    }
    

    public ArrayList<Action> getList() {
        return list;
    }

    public void setList(ArrayList<Action> list) {
        this.list = list;
    }

    public Action get(String actionTitle) {
        for(Action action : list){
            if(action.title.equalsIgnoreCase(actionTitle)){
                return action;
            }
        }
        return null;
    }

    public boolean has(String actionId) {
        for(Action action : list){
            if(action.title.equalsIgnoreCase(actionId)){
                return true;
            }
        }
        return false;
    }
    
    
    
}
