/*
 * Define a context for a web connection
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.web;

import online.nostrium.user.User;
import online.nostrium.main.core;
import online.nostrium.servers.terminal.notifications.ClientType;
import online.nostrium.servers.terminal.notifications.Session;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.screens.Screen;

/**
 * @author Brito
 * @date:2024-08-27
 * @location: Germany
 */
public class ContextSession {
    
    final Screen screen;
    User user;
    TerminalApp app;
    final String uniqueId;
    final Session session;

    public ContextSession(Screen screen, User user, TerminalApp app, String uniqueId) {
        this.screen = screen;
        this.user = user;
        this.app = app;
        this.uniqueId = uniqueId;
        // register this visitor as a session
        session = new Session(ClientType.WEB, app, user);
        core.sessions.addSession(session);
    }
    
    public void ping(){
        // Show that the session is still active
        session.ping();
    }

    public Screen getScreen() {
        return screen;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TerminalApp getApp() {
        return app;
    }

    public void setApp(TerminalApp app) {
        this.app = app;
    }
    
    

}
