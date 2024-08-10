/*
 * An active session
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.notifications;

import java.util.Date;
import online.nostrium.servers.apps.user.User;
import online.nostrium.servers.terminal.TerminalApp;

/**
 * Author: Brito
 * Date: 2024-08-10
 * Location: Germany
 */
public class Session {
    
    final Date sessionStarted;        // when it was started
    Date sessionLastActive;     // when it was last doing something
    final TerminalApp app;            // the root app
    User user;                  // who is using this
    final ClientType clientType;      // what kind of terminal is being used   

    public Session(ClientType clientType, TerminalApp app, User user) {
        this.app = app;
        this.clientType = clientType;
        this.sessionStarted = new Date();
        this.sessionLastActive = new Date();
        this.user = user;
    }

    /**
     * Ping to make sure it is still alive this session
     */
    public void ping() {
        this.sessionLastActive = new Date();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getSessionStarted() {
        return sessionStarted;
    }

    public Date getSessionLastActive() {
        return sessionLastActive;
    }

    public TerminalApp getApp() {
        return app;
    }

    public User getUser() {
        return user;
    }

    public ClientType getClientType() {
        return clientType;
    }
    
    
    
    public void sendNotification(String appName, String id, 
            NotificationType sessionType, Object object){
        // e.g.: chat | root | update | object
        
    }
    

}
