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
    
    
    public void add(String eventId) {
        // don't override an existing event
        if(list.containsKey(eventId)){
            return;
        }
        // add the new event
        Event event = new Event();
        list.put(eventId, event);
    }
    
    public void triggerBefore(String eventId, Object object){
        Event event = list.get(eventId);
        if(event == null){
            event = new Event();
            list.put(eventId, event);
        }
        event.before(object);
    }
    
    /**
     * Provides an object for the first action that matches the eventId
     * to provide an another object based on the input
     * @param eventId
     * @param object
     * @return all good when ActionType == NOTHING 
     */
    public ActionResult getResult(String eventId, Object object){
        Event event = list.get(eventId);
        if(event == null){
            event = new Event();
        }
        
        ActionResult output = new ActionResult(ActionType.NOTHING, "", null);
        for(Action action : event.getActions()){
            ActionResult result = action.doAction(object);
            if(result.getType() != ActionType.NOTHING){
                output = result;
            }
        }
        return output;
    }
    
    public void triggerAfter(String eventId, Object object){
        Event event = list.get(eventId);
        if(event == null){
            event = new Event();
            list.put(eventId, event);
        }
        event.after(object);
    }

    /**
     * Add action that will be triggered by a specific event id
     * @param eventId
     * @param action 
     */
    public void addAction(String eventId, Action action) {
        // create the event id in case it doesn't exist
        if(list.containsKey(eventId) == false){
            add(eventId);
        }
        // add the actions
        Event event = list.get(eventId);
        event.add(action);
    }

    
}
