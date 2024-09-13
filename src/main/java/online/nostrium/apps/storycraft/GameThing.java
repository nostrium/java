/*
 * Used for opponents, items and so forth
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.util.HashMap;
import online.nostrium.utils.TextFunctions;

/**
 * @author Brito
 * @date: 2024-09-05
 * @location: Germany
 */
public abstract class GameThing {

    protected String name, // The name of the item
            id,         // machine readable name
            textImage = null;

    // attributes are added to the user overall sum
    HashMap<String, Object> attributes = new HashMap<>();

    /**
     * Constructor for an item with both durability and number of usages.
     *
     */
    public GameThing() {
    }

    @Override
    public String toString() {
        return "Thing {"
                + "name='" + name + '\''
                + '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, Object> attributes) {
        this.attributes = attributes;
    }

    public void addAttributes(HashMap<String, String> atts) {
        for (String key : atts.keySet()) {
            String value = atts.get(key);
            this.attributes.put(key, value);
        }
    }

    
    public boolean parse(String textBlock, String anchor, String thingId) {
        return this.parse(null, textBlock, anchor, thingId);
    }
    
    public boolean parse(
            Scene scene, String textBlock, String anchor, String thingId) {
//        # Item: Ancient Shield
//        Type: Shield  
//        Description: A shield from a bygone era, worn but sturdy.  
//        Defense Bonus: 5  
//        Durability: 20

        processedSpecificBlock(scene, textBlock);
        
        // get the image
        if(textBlock.contains("```\n")){
            this.textImage = TextFunctions.extractTextBetweenTicks(textBlock);
        }

        String[] lines = textBlock.split("\n");
        for (String line : lines) {
            // fixed values
            if (processedSpecificLine(scene, line, attributes)) {
                continue;
            }
            // get the item name
            if (line.startsWith(anchor)) {
                name = line.substring(anchor.length() //+ ": ".length()
                );
                id = thingId + name.toLowerCase().replace(" ", "-");
                id = id.replace(":", "");
                continue;
            }
            // variable attributes
            // e.g - Durability: 10
            if (line.startsWith("- ") && line.contains(": ")) {
                int i = line.indexOf(": ");
                String key = line.substring("- ".length(), i);
                String valueText = line.substring(i + ": ".length());
                
                try{
                    int value = Integer.parseInt(valueText);
                    attributes.put(key, value);
                }catch(NumberFormatException e){
                    attributes.put(key, valueText);
                }
                
                continue;
            }
            // define variable attributes without using "- " on line start
            // e.g Durability: 10
            if (line.contains(": ")) {
                int i = line.indexOf(": ");
                String key = line.substring(0, i);
                String valueText = line.substring(i + ": ".length());
                //int value = Integer.parseInt(valueText);
                attributes.put(key, valueText);
                continue;
            }

            // one empty line breaks the item data
            if (line.isEmpty()) {
                break;
            }
        }
        // the item needs to have the minimum sets of data
        if (name == null //|| description == null || type == null
                || attributes.isEmpty()) {
            return false;
        }

        return true;
    }

    protected abstract boolean processedSpecificLine(Scene scene, String line, HashMap<String, Object> atts);

    protected abstract void processedSpecificBlock(Scene scene, String textBlock);

    public Object getAttribute(String key){
        if(attributes.containsKey(key) == false){
            return null;
        }
        return attributes.get(key);
    }
    
    public String getTextImage() {
        return textImage;
    }
   
}
