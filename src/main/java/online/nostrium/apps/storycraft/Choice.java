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
    
    final int chance;

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

    public int getChance() {
        return chance;
    }
    
    
}
