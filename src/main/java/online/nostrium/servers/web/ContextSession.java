/*
 * Define a context for a web connection
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.web;

import online.nostrium.apps.basic.TerminalBasic;
import online.nostrium.user.User;
import online.nostrium.main.core;
import online.nostrium.session.ChannelType;
import online.nostrium.session.Session;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.utils.screens.Screen;

/**
 * @author Brito
 * @date:2024-08-27
 * @location: Germany
 */
public class ContextSession {
    
    final Screen screen;
    User user;
    TerminalApp app;
    final String sessionId;
    final Session session;

    public ContextSession(Screen screen, User user, String uniqueId) {
        this.screen = screen;
        this.user = user;
        this.sessionId = uniqueId;
        // register this visitor as a session
        session = new Session(ChannelType.WEB, sessionId);
        app = new TerminalBasic(session);
        session.setup(app, user, screen);
                
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
        return sessionId;
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
