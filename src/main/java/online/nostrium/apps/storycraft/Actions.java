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
            parseBlock(block);
        }
    }

    /**
     * Extracts the needed information from the block
     * @param block 
     */
    private void parseBlock(String block) {
        Action action = new Action();
        
        String[] lines = block.split("\n");
        for(String line : lines){
            // action trigger
            if(line.startsWith(anchor)){
                String trigger = line.substring(anchor.length());
                action.setAction(trigger);
                continue;
            }
            // description
            if(line.startsWith("> ")){
                String description = line.substring("> ".length());
                action.setDescription(description);
                continue;
            }
            // expression to parse
            if(line.startsWith("- ")){
                action.addRule(line.substring("- ".length()));
            }
            
            // reached the next section?
            if(line.startsWith("#")){
                break;
            }
            
        }
        if(action.getRules().isEmpty()){
            return;
        }
        // add the list to our action
        list.add(action);
    }

    public ArrayList<Action> getList() {
        return list;
    }

    public void setList(ArrayList<Action> list) {
        this.list = list;
    }

    public Action get(String actionId) {
        for(Action action : list){
            if(action.getAction().equalsIgnoreCase(actionId)){
                return action;
            }
        }
        return null;
    }

    public boolean has(String actionId) {
        for(Action action : list){
            if(action.getAction().equalsIgnoreCase(actionId)){
                return true;
            }
        }
        return false;
    }
    
    
    
}
