/*
 * Events
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.utils.events;

import java.util.HashMap;

/**
 * @author Brito
 * @date: 2024-09-02
 * @location: Germany
 */
public class Events {

    private final HashMap<String, Event> list = new HashMap<>();
    
    public void add(String eventId, Event event){
        list.put(eventId, event);
    }
    
    public void triggerBefore(String eventId){
        Event event = list.get(eventId);
        if(event == null){
            event = new Event();
            list.put(eventId, event);
        }
        event.before();
    }
    
    public void triggerAfter(String eventId){
        Event event = list.get(eventId);
        if(event == null){
            event = new Event();
            list.put(eventId, event);
        }
        event.after();
    }
    
}
