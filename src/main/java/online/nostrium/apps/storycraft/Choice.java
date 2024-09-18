/*
 * Option inside the game
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.util.ArrayList;
import online.nostrium.logs.Log;
import online.nostrium.servers.terminal.TerminalCode;

/**
 * @author Brito
 * @date: 2024-09-04
 * @location: Germany
 */
public class Choice {

    String title;
    String link;

    LinkType linkType;
    String nextActions = null;

    ArrayList<String> actionsWin = new ArrayList<>();
    ArrayList<String> actionsLose = new ArrayList<>();

    int chance;

    public Choice() {
    }

    public Choice(String title, String link, LinkType linkType) {
        this.title = title;
        this.link = link;
        this.linkType = linkType;
        chance = 0;
    }

    public Choice(String title, String link, int chance, LinkType linkType) {
        this.title = title;
        this.link = link;
        this.linkType = linkType;
        this.chance = chance;
    }

    public void parse(String line) {
        // - [Fight the ogre](#opponent-ogre) -> win:#items-coins-100; #scene-victory; Lose:#scene-end
        int y = line.indexOf("]");
        int x = line.indexOf("#");
        int xx = line.indexOf(")");
        title = line.substring("- [".length(), y);
        link = line.substring(x + 1, xx);//.replace("(", "").replace(")", "").trim();

        // follow-up actions
        String followup = "-> ";
        if (line.contains(followup)) {
            // win:#items-coins-100; #scene-victory; Lose:#scene-end
            int z = line.indexOf(followup);
            nextActions = line.substring(z + followup.length());
        }

        // generate clean actions 
        processPostActions(nextActions);

        if (link.startsWith("item-")) {
            linkType = LinkType.ITEM;
        } else if (link.startsWith("scene-")) {
            linkType = LinkType.SCENE;
        } else if (link.startsWith("opponent-")) {
            linkType = LinkType.ACTION;
        } else {
            linkType = LinkType.OTHER;
        }

    }

    private void processPostActions(String line) {
        // win:#items-coins-100; #scene-victory; lose:#scene-end
        String anchorWin = "win:";
        String anchorLose = "lose:";

        extractActions(line, anchorWin, anchorLose, actionsWin);
        extractActions(line, anchorLose, anchorWin, actionsLose);
    }

    private void extractActions(String data, String anchor1, String anchor2, ArrayList<String> action1) {
        if(data == null){
            Log.write("STORYCRAFT", TerminalCode.INVALID, "Invalid data for choices", null);
            return;
        }
        String dataLowercase = data.toLowerCase();
        if (dataLowercase.contains(anchor1) == false) {
            return;
        }
        int i = dataLowercase.indexOf(anchor1);
        String temp = data.substring(i + anchor1.length());
        String tempLowercase = temp.toLowerCase();
        
        if (tempLowercase.contains(anchor2)) {
            int x = tempLowercase.indexOf(anchor2);
            temp = temp.substring(0, x).trim();
            tempLowercase = temp;
        }
        
        // #items-coins-100; #scene-victory;
        String[] lines = temp.split(";");
        for(String line : lines){
            line = line.trim();
            if(line == null || line.isBlank()){
                continue;
            }
            action1.add(line);
        }
    }

    public void setNextActions(String nextActions) {
        this.nextActions = nextActions;
    }

    public ArrayList<String> getActionsWin() {
        return actionsWin;
    }

    public ArrayList<String> getActionsLose() {
        return actionsLose;
    }

    @Override
    public String toString() {
        return title;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public int getChance() {
        return chance;
    }

    public String getNextActions() {
        return nextActions;
    }

}
