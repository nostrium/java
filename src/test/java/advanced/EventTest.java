/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package advanced;

import static advanced.EventTest.eventId;
import online.nostrium.main.core;
import online.nostrium.utils.events.Action;
import online.nostrium.utils.events.ActionResult;
import online.nostrium.utils.events.ActionType;
import online.nostrium.utils.events.Event;
import online.nostrium.utils.events.Events;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * This platform is growing in complexity and needs to permit apps to interact
 * the actions of other apps. So we define here an event based structure for
 * apps to listen and react to specific events by other apps.
 *
 * The advantage is that we do not couple directly the apps. You can register to
 * hear the specific events from an app but if that app is not installed then
 * you don't break the system. You just don't trigger than action until it
 * becomes triggered by the app one day when installed.
 *
 * @author Brito
 * @date: 2024-09-28
 * @location: Germany
 */
public class EventTest {
    
    // the id we use for the test
    final static String eventId = "sayHello";

    public EventTest() {
    }

    @Test
    public void helloEvents() {

        // setup the basic configuration of the platform
        core.startConfig();

        // the controller for events
        Events events = new Events();
        events.add(eventId);
        
        // app that creates an action and adds this to events
        AppExample app1 = new AppExample("1", events);

        // trigger the action
        ActionResult result = events.getResult(eventId, null);
        System.out.println(result.getMessage());
        
        System.gc();
        
        
    }
}

class AppExample {

    final Events events;
    Action action;

    public AppExample(String id, Events events) {
        this.events = events;
        // create an action
        action = new Action() {
            @Override
            protected String getId() {
                return "action" + id;
            }

            @Override
            protected ActionResult before(Object object) {
                String message = id + ": Called before";
                return new ActionResult(ActionType.OK, message, null);
            }

            @Override
            protected ActionResult after(Object object) {
                String message = id + ": Called after";
                return new ActionResult(ActionType.OK, message, null);
            }

            @Override
            protected ActionResult doAction(Object object) {
                String message = id + ": action!";
                return new ActionResult(ActionType.OK, message, null);
            }
        };

        events.addAction(eventId, action);
    }

}
