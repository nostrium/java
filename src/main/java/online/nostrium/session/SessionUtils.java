/*
 * Utils related to sessions
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.session;

import online.nostrium.apps.basic.TerminalBasic;
import online.nostrium.main.core;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;
import online.nostrium.utils.screens.Screen;

/**
 * @author Brito
 * @date: 2024-09-29
 * @location: Germany
 */
public class SessionUtils {

    /**
     * 
     * @param channelType the channel being used
     * @param sessionId
     * @param screen
     * @return 
     */
    public static Session getOrCreateSession(ChannelType channelType, String sessionId, Screen screen){
        if(core.sessions.has(channelType, sessionId)){
            // get the existing session
            Session session = core.sessions.get(channelType, sessionId);
            session.ping();
            return session;
        }else{
            // create a new session
            Session session = new Session(channelType, sessionId);
            // setup an anonymous user at the beginning
            User user = UserUtils.createUserAnonymous();
            session.setUser(user);
            TerminalApp app = new TerminalBasic(session);
            
            session.setup(app, user, screen);
            
            // add this session to the monitoring
            core.sessions.addSession(session);
            session.ping();
            return session;
        }
        
    }
    
    
}
