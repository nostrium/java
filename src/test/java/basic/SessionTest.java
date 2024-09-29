/*
 *  Test the folder functions
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package basic;

import online.nostrium.session.ClientType;
import online.nostrium.session.NotificationType;
import online.nostrium.session.Session;
import online.nostrium.session.Sessions;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.utils.screens.Screen;
import online.nostrium.utils.screens.ScreenCLI;
import static online.nostrium.utils.TestingFunctions.createFakeApp;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Date: 2023-02-08
 * Place: Germany
 * @author brito
 */
public class SessionTest {
    
    public SessionTest() {
    }

    
    @Test
    public void helloSession() {
        
        // create the session holders
        Sessions sessions = new Sessions();
        String id = "me3";
        
        // create the users
        User user1 = UserUtils.createUserAnonymous();
        Screen screen1 = new ScreenCLI();
        TerminalApp app1 = createFakeApp(screen1, user1, "chat", id);
        Session session1 = new Session(ClientType.TELNET, app1, user1);
        sessions.addSession(session1);
        
        User user2 = UserUtils.createUserAnonymous();
        Screen screen2 = new ScreenCLI();
        TerminalApp app2 = createFakeApp(screen2, user2, "chat", id);
        Session session2 = new Session(ClientType.TELNET, app2, user2);
        sessions.addSession(session2);
        
        
        // send the first notification, for example a new chat line
        String message = "Hello!";
        String appId = app1.getId();
        sessions.sendNotification(appId, user1, NotificationType.UPDATE, message);
        // this should have arrived to session 2 also
        String result = app2.getDescription();
        assertEquals(message, result);
        
        
    }
}
