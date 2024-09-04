/*
 * Actions
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.utils.events;

/**
 * 
 * An event was triggered, this class will be executed.
 * 
 * @author Brito
 * @date: 2024-09-02
 * @location: Germany
 */
public abstract class Action {
    
    long timeToRunBefore = 0;       // how long did it took to execute?
    long timeToRunAfter = 0;        // how long did it took to execute?
    long timeToRunTotal = 0;        // total value combined
    
    // add runtime messages as needed
    public MessageQueue messages = new MessageQueue(10);
    
    boolean separateThread = false; // run on a separate thread
    
    protected abstract String getId();
    protected abstract ActionResult before();
    protected abstract ActionResult after();

}
