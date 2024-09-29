/*
 * Keep track of the active sessions
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.session;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import online.nostrium.user.User;

/**
 * @author Brito
 * @date: 2024-08-10
 * @location: Germany
 */
public class Sessions {

    private final Queue<Session> list = new LinkedList<>();

    public void addSession(Session session) {
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

    public void removeSession(Session sessionToRemove) {
        Iterator<Session> iterator = list.iterator();
        while (iterator.hasNext()) {
            Session session = iterator.next();
            if (session.equals(sessionToRemove)) {
                session.sendStop(); // Stop the session
                iterator.remove();  // Remove the session from the list
                break; // Exit the loop since the session has been found and removed
            }
        }
    }

    public int countSessions() {
        return list.size();
    }

    /**
     * Look inside all available sessions to see if any of them
     * contains the same sessionId and sessionType. When a match
     * is found then it will resume the session.
     * @param channelType
     * @param sessionId
     * @return 
     */
    public boolean has(ChannelType channelType, String sessionId) {
        for(Session session : list){
            if(session.channelType == channelType
                    && session.sessionId.equals(sessionId)){
                return true;
            }
        }
        return false;
    }

    public Session get(ChannelType channelType, String sessionId) {
        for(Session session : list){
            if(session.channelType == channelType
                    && session.sessionId.equals(sessionId)){
                return session;
            }
        }
        return null;
    }
}
