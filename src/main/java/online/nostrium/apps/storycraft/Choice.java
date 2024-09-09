/*
 * Option inside the game
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

/**
 * @author Brito
 * @date: 2024-09-04
 * @location: Germany
 */
public class Choice {

    final String title;
    final String link;

    final LinkType linkType;
    String nextActions = null;

    int chance;

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

    public static Choice parse(String line) {
        // - [Take](#item-golem-heart)
        int y = line.indexOf("]");
        int x = line.indexOf("#");
        String nextActionToWrite = null;
        String choiceTitle = line.substring("- [".length(), y);
        String choiceLink = line.substring(x + 1).replace("(", "").replace(")", "").trim();

        if(choiceLink.contains(": ")){
            int z = choiceLink.indexOf(": ");
            nextActionToWrite = choiceLink.substring(z + ": ".length());
            choiceLink = choiceLink.substring(0, z);
        }
        
        LinkType linkType;
        if (choiceLink.startsWith("item-")) {
            linkType = LinkType.ITEM;
        } else if (choiceLink.startsWith("scene-")) {
            linkType = LinkType.SCENE;
        } else if (choiceLink.startsWith("opponent-")) {
            linkType = LinkType.FIGHT;
        } else {
            linkType = LinkType.OTHER;
        }
        
        Choice choice = new Choice(choiceTitle, choiceLink, linkType);
        choice.setNextActions(nextActionToWrite);
        return choice;
    }

    public void setNextActions(String nextActions) {
        this.nextActions = nextActions;
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
