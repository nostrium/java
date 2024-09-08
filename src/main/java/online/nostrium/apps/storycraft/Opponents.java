/*
 * List of the opponents until a better name is found
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.storycraft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Brito
 * @date: 2024-09-08
 * @location: Germany
 */
public class Opponents {

    private final Map<String, Opponent> opponents = new HashMap<>();
    
    public void parse(String text) {
        String anchor = "# Opponent: ";
        String anchorId = "opponent-";
        ArrayList<String> blocks = StoryUtils.getTextBlocks(anchor, text);
        if (blocks.isEmpty()) {
            return;
        }
        for (String itemBlock : blocks) {
            Opponent op = new Opponent();
            boolean result = op.parse(null, itemBlock, anchor, anchorId);
            if (result == false) {
                continue;
            }
            // add the item
            addOpponent(op);
            opponents.put(op.id, op);
        }
    }
    

    public void addOpponent(Opponent op) {
        opponents.put(op.getId(), op);
    }    
    
    
    public Map<String, Opponent> getOpponents() {
        return opponents;
    }
}

