/*
 * Equip the user with an item, including durability, usage effects, and type limitations.
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.util.HashMap;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class Item extends GameThing{
    
    private String
            description,    // A brief description of the item
            type;           // The type of item (e.g., "Weapon", "Shield", "Ring")
    
    protected int
            usageLeft = 0;  // The number of times the item can be used (e.g., healing potion)
    

    /**
     * Constructor for an item with both durability and number of usages.
     */
    public Item() {
        super();
    }
    
    @Override
    public String toString() {
        return "Item {" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\''
                +
                '}';
    }
    
    
    @Override
    protected boolean processedSpecificLine(Scene scene, String line, HashMap<String, String> atts) {
            if (line.startsWith("Description: ")) {
                description = line.substring("Description: ".length());
                return true;
            }
            if (line.startsWith("Type: ")) {
                type = line.substring("Type: ".length());
                return true;
            }
        return false;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUsageLeft() {
        return usageLeft;
    }

    public void setUsageLeft(int usageLeft) {
        this.usageLeft = usageLeft;
    }

    @Override
    protected void processedSpecificBlock(Scene scene, String textBlock) {
    }

    
}
