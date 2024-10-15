/*
 * An active session
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.session;

import java.util.Date;
import online.nostrium.user.User;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.web.App;
import online.nostrium.session.maps.Map;
import online.nostrium.session.maps.MapApp;
import online.nostrium.session.maps.MapBox;
import online.nostrium.utils.screens.Screen;

/**
 * @Author: Brito
 * @Date: 2024-08-10
 * @Location: Germany
 */
public class Session {

    final String sessionId;     // unique identifier of session that is running
    final Date sessionStarted;  // when it was started
    final ChannelType channelType;  // what kind of terminal is being used
    Date sessionLastActive;     // when it was last doing something
    App app;            // the current app
    User user;                  // who is using this
    Screen screen;
    private boolean timeToStop = false;
    private MapBox map = null;
    private Map currentLocation = null;
    

    public Session(
            ChannelType clientType, String sessionId,
            TerminalApp app, User user, Screen screen) {
        this.sessionId = sessionId;
        this.app = app;
        this.channelType = clientType;
        this.sessionStarted = new Date();
        this.sessionLastActive = new Date();
        this.user = user;
        this.screen = screen;
        map = new MapApp(app);
        map.index();
        currentLocation = map;
    }

    public Session(ChannelType clientType, String sessionId) {
        this.sessionId = sessionId;
        this.channelType = clientType;
        this.sessionStarted = new Date();
        this.sessionLastActive = new Date();
    }

    public void setup(TerminalApp app, User user, Screen screen) {
        this.app = app;
        this.user = user;
        this.screen = screen;
        // index the app
        map = new MapApp(app);
        map.index();
        currentLocation = map;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Screen getScreen() {
        return screen;
    }

    /**
     * Ping to make sure it is still alive this session
     */
    public void ping() {
        this.sessionLastActive = new Date();
    }

    public void setUser(User user) {
        this.user = user;
        // need to trigger a new indexing
        if(map!= null){
            map.index();
            setCurrentLocation(map);
        }
    }

    public Map getMap() {
        return map;
    }

    public Date getSessionStarted() {
        return sessionStarted;
    }

    public Date getSessionLastActive() {
        return sessionLastActive;
    }

    public App getApp() {
        return app;
    }

    public User getUser() {
        return user;
    }

    public ChannelType getClientType() {
        return channelType;
    }

    public boolean isTimeToStop() {
        return timeToStop;
    }

    public void sendStop() {
        timeToStop = true;
    }

    boolean hasId(String id) {
        String appId = app.getPathVirtual();
        return appId.equalsIgnoreCase(id);
    }

    public void setApp(App app) {
        this.app = app;
    }

    public long getLastPing() {
        return this.sessionLastActive.getTime();
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public Map getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Define where the user is currently located
     * @param currentLocation 
     */
    public void setCurrentLocation(Map currentLocation) {
        this.currentLocation = currentLocation;
        // update the app currently active
        if(this.currentLocation instanceof MapApp){
            MapApp mapApp = (MapApp) this.currentLocation;
            this.setApp(mapApp.getAppRelated());
        }
    }

}
