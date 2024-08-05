/*
 *  Defines affiliate/friend links displayed on the front page
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package network.nostrium.forum.structures;

/**
 * Date: 2023-02-08
 * Place: Germany
 * @author brito
 */
public class Level2 implements Level{

    @Override
    public int id() {
        return 2;
    }

    @Override
    public int maxNotesPerDay() {
        return 10;
    }

    @Override
    public String description() {
        return "";
    }

    
    
}
