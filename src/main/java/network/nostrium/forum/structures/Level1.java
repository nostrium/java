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
public class Level1 implements Level{

    @Override
    public int id() {
        return 1;
    }

    @Override
    public int maxNotesPerDay() {
        return 1;
    }

    @Override
    public String description() {
        return "";
    }

    
    
}
