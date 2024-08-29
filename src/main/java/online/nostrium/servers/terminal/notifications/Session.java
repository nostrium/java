/*
 * An active session
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.terminal.notifications;

import java.util.Date;
import online.nostrium.apps.user.User;
import online.nostrium.servers.terminal.TerminalApp;

/**
 * Author: Brito
 * Date: 2024-08-10
 * Location: Germany
 */
public class Session {
    
    final Date sessionStarted;        // when it was started
    Date sessionLastActive;     // when it was last doing something
    TerminalApp app;            // the current app
    User user;                  // who is using this
    final ClientType clientType;      // what kind of terminal is being used
    private boolean timeToStop = false;

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

    public boolean isTimeToStop() {
        return timeToStop;
    }

    public void sendStop() {
        timeToStop = true;
    }

    boolean hasId(String id) {
        String appId = app.getId();
        return appId.equalsIgnoreCase(id);
    }

    public void setApp(TerminalApp app) {
        this.app = app;
    }
    

}
