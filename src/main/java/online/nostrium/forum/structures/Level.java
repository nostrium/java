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
public interface Level {

    public int id();
    public int maxNotesPerDay();
    public String description();
    
}
