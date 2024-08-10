/*
 * Keep track of the active sessions
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.notifications;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import online.nostrium.servers.apps.user.User;

/**
 * Author: Brito
 * Date: 2024-08-10
 * Location: Germany
 */
public class Sessions {
    
    private final Queue<Session> list = new LinkedList<>();
    
    public void addSession(Session session){
        list.add(session);
    }

    
   public void sendNotification(
           String id,
           User userSender, 
           NotificationType notificationType, 
           Object object) {
        // e.g.: chat | root | update | object
        
        Iterator<Session> iterator = list.iterator();
        while (iterator.hasNext()) {
            Session session = iterator.next();
            
            // Calculate the time difference between now and when the session started
            long diffInMillis = new Date().getTime() - session.getSessionStarted().getTime();
            long daysPassed = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            
            // When the session is older than 5 days, stop it and remove it
            if (daysPassed > 5) {
                session.sendStop();
                iterator.remove();
                continue;
            }
            
            // Found a match
            if (session.hasId(id)) {
                session.app.receiveNotification(userSender, notificationType, object);
            }
        }
    }
        
        
}
