/*
 * Stores a record for a user
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.games.guess_number;

/**
 * @author Brito
 * @date: 2024-08-25
 * @location: Germany
 */
public class UserRecordGuess {
    
    final int attempts;
    final String userNpub;
    final long timestamp;
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
