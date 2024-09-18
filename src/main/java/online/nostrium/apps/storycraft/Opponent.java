/*
 * Defines an opponent
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.storycraft;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Brito
 * @date: 2024-09-05
 * @location: Germany
 */
public class Opponent  extends GameThing{

    public Opponent() {
        super();
    }
    
    ArrayList<Choice> matchWin = new ArrayList<>();
    ArrayList<Choice> matchLose = new ArrayList<>();
    ArrayList<Choice> matchRun = new ArrayList<>();
    
    final String 
        ifWin = "If win",
        ifLose = "If lose",
        ifRun = "If run";

    @Override
    protected boolean processedSpecificLine(Scene scene, String line, HashMap<String, Object> atts) {
        
        return false;
    }

    @Override
    protected void processedSpecificBlock(Scene scene, String textBlock) {
        // on this block we are searching for IF conditions
        
        parseIfCondition(ifWin, matchWin, textBlock);
        parseIfCondition(ifLose, matchLose, textBlock);
        parseIfCondition(ifRun, matchRun, textBlock);
        
    }

    private void parseIfCondition(
            String ifWin, ArrayList<Choice> list, String textBlock) {
//        ## If win:
//        - [Take](#item-golem-heart)
//        - [Take](#item-coins-20-30)
//        - [Exit the ruins](#scene-exit-ruins)
        String idSpecific = "## " + ifWin + ":";
        String block = StoryUtils.getTextBlock(idSpecific, textBlock);
        if(block == null || block.isEmpty()){
            return;
        }
        // transform to lines
        String[] lines = block.split("\n");
        for(String line : lines){
            if(line.startsWith("- [") == false){
                continue;
            }
            Choice choice = new Choice();
            choice.parse(line);
            if(choice == null){
                continue;
            }
            // add to the list
            list.add(choice);
        }
        
    }

    public ArrayList<Choice> getMatchWin() {
        return matchWin;
    }

    public ArrayList<Choice> getMatchLose() {
        return matchLose;
    }

    public ArrayList<Choice> getMatchRun() {
        return matchRun;
    }

    public String[] getActions() {
        String id = "Actions";
        if(attributes.containsKey(id) == false){
            return null;
        }
        String text = (String) this.attributes.get("Actions");
        return text.split("; ");
    }


}
