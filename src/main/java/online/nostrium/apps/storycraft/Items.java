/*
 * A bag of items
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.storycraft;

import java.util.ArrayList;

/**
 * @author Brito
 * @date: 2024-09-08
 * @location: Germany
 */
public class Items {

    ArrayList<Item> list = new ArrayList<>();
    
    public void parse(String sceneText) {
        String anchor = "# Item: ";
        String anchorId = "item-";
        ArrayList<String> itemBlocks = StoryUtils.getTextBlocks(anchor, sceneText);
        if (itemBlocks.isEmpty()) {
            return;
        }
        for (String itemBlock : itemBlocks) {
            Item item = new Item();
            boolean result = item.parse(null, itemBlock, anchor, anchorId);
            if (result == false) {
                continue;
            }
            // add the item
            addItem(item);
        }
    }
    
     public Item getItem(String link) {
        for(Item item : list){
            if(item.getId().equalsIgnoreCase(link)){
                return item;
            }
        }
        return null;
    }

    private void addItem(Item itemToAdd) {
        String id = itemToAdd.getId();
        for(Item item : list){
            if(item.getId().equalsIgnoreCase(id)){
                return;
            }
        }
        // add it up
        list.add(itemToAdd);
    }
    
}
