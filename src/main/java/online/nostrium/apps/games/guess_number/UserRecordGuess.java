/*
 * Stores a record for a user
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.games.guess_number;

import com.google.gson.annotations.Expose;

/**
 * @author Brito
 * @date: 2024-08-25
 * @location: Germany
 */
public class UserRecordGuess {
    
    @Expose
    final int attempts;
    
    @Expose
    final String userNpub;
    
    @Expose
    final long timestamp;
    
    @Expose
    final long timeRequired;

    public UserRecordGuess(
            int attempts, String userNpub, 
            long timestamp, long timeRequired) {
        this.attempts = attempts;
        this.userNpub = userNpub;
        this.timestamp = timestamp;
        this.timeRequired = timeRequired;
    }
    
    

}
