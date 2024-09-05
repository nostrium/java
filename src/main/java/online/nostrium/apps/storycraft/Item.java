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
public class Item {

    String name;         // The name of the item
    String id;           // machine readable name
    String description;  // A brief description of the item
    String type;         // The type of item (e.g., "Weapon", "Shield", "Ring")
    int usageLeft = 0;   // The number of times the item can be used (e.g., healing potion)
    
    // attributes are added to the user overall sum
    HashMap<String, Integer> attributes = new HashMap<>();
    

    /**
     * Constructor for an item with both durability and number of usages.
     *
     * @param name        the name of the item
     * @param description a brief description of the item
     * @param type        the type of the item (e.g., "Weapon", "Shield", "Ring")
     */
    public Item(String name, String description, String type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

//    /**
//     * Use the item in combat. This method decreases durability by 1 each time the item is used.
//     */
//    public void useInCombat() {
//        if (durability > 0) {
//            durability--;
//            writeln("%s used in combat. Durability now: %s", name, Integer.toString(durability));
//        } else {
//            writeln("%s is broken and can't be used.", name);
//        }
//    }
//
//    /**
//     * Use the item for a special effect, such as healing. This method decreases the number of usages left.
//     *
//     * @return true if the item was successfully used, false if no usages left.
//     */
//    public boolean useForEffect() {
//        if (usagesLeft > 0) {
//            usagesLeft--;
//            writeln("%s used for effect. Usages left: %s", name, Integer.toString(usagesLeft));
//            return true;
//        } else {
//            writeln("%s is empty and can't be used.", name);
//            return false;
//        }
//    }
//
//    /**
//     * Check if the item is still usable based on its durability and number of usages left.
//     *
//     * @return true if the item can still be used, false otherwise.
//     */
//    public boolean isUsable() {
//        return durability > 0 || usagesLeft > 0;
//    }

    @Override
    public String toString() {
        return "Item {" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\''
                +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUsageLeft() {
        return usageLeft;
    }

    public void setUsageLeft(int usageLeft) {
        this.usageLeft = usageLeft;
    }

    public HashMap<String, Integer> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, Integer> attributes) {
        this.attributes = attributes;
    }
    
    

    public void addAttributes(HashMap<String, Integer> atts) {
        for(String key : atts.keySet()){
            int value = atts.get(key);
            this.attributes.put(key, value);
        }
    }

}
