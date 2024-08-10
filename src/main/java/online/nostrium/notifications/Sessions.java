/*
 * Keep track of the active sessions
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.notifications;

import java.util.LinkedList;
import java.util.Queue;
import online.nostrium.servers.terminal.TerminalApp;

/**
 * Author: Brito
 * Date: 2024-08-10
 * Location: Germany
 */
public class Sessions {
    
    private Queue<Session> queue = new LinkedList<>();
    
    public void addSession(TerminalApp app){
        Session session = new Session(app, );
        session.app
    }

}
