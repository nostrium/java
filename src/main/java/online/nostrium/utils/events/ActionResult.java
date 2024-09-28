/*
 * Result from running an event
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.utils.events;

/**
 * @author Brito
 * @date: 2024-09-02
 * @location: Germany
 */
public class ActionResult {
    final ActionType type;
    final String message;
    final Object object;

    public ActionResult(ActionType type, String message, Object object) {
        this.type = type;
        this.message = message;
        this.object = object;
    }

    public ActionType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Object getObject() {
        return object;
    }
    
    
}
