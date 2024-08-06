/*
 *  Defines affiliate/friend links displayed on the front page
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.forum.structures;

/**
 * Date: 2023-02-08
 * Place: Germany
 * @author brito
 */
public class Level3 implements Level{

    @Override
    public int id() {
        return 3;
    }

    @Override
    public int maxNotesPerDay() {
        return 50;
    }

    @Override
    public String description() {
        return "";
    }

    
    
}
