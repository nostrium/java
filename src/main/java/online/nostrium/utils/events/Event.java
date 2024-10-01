/*
 * Event
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.utils.events;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Brito
 * @date: 2024-09-02
 * @location: Germany
 */
public class Event {
    
    private final List<Action> actions = new ArrayList<>();
    
    public void add(Action action){
        for(Action actionExisting : actions){
            if(actionExisting.getId().equals(action.getId())){
                return;
            }
        }
        actions.add(action);
    }
    
    public void remove(String id){
        Action actionToRemove = null;
        for(Action actionExisting : actions){
            if(actionExisting.getId().equals(id)){
                actionToRemove = actionExisting;
                break;
            }
        }
        if(actionToRemove != null){
            actions.remove(actionToRemove);
        }
    }
    
    
    /**
     * Starts all the actions before this event
     */
    public ActionResult getAction(Object object){
        for(Action action : actions){
            long time = System.currentTimeMillis();
            ActionResult result = action.doAction(object);
            action.timeToRunAfter = System.currentTimeMillis() - time;
            return result;
        }
        return null;
    }
    
    /**
     * Starts all the actions before this event
     * @param object
     * @return 
     */
    public void before(Object object){
        for(Action action : actions){
            long time = System.currentTimeMillis();
            action.before(object);
            action.timeToRunAfter = System.currentTimeMillis() - time;
        }
    }
    
    /**
     * Starts all the actions after this event
     */
    public void after(Object object){
        for(Action action : actions){
            long time = System.currentTimeMillis();
            action.after(object);
            action.timeToRunAfter = System.currentTimeMillis() - time;
        }
    }

    public List<Action> getActions() {
        return actions;
    }
}
