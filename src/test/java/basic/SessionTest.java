/*
 *  Test the folder functions
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package basic;

import online.nostrium.notifications.ClientType;
import online.nostrium.notifications.NotificationType;
import online.nostrium.notifications.Session;
import online.nostrium.notifications.Sessions;
import online.nostrium.apps.user.User;
import online.nostrium.apps.user.UserUtils;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalUtils;
import online.nostrium.servers.terminal.screens.Screen;
import online.nostrium.servers.terminal.screens.ScreenTesting;
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

    
    static TerminalApp createFakeApp(Screen screen, User user, String appName, String id){
        
        TerminalApp app = new TerminalApp(screen, user) {
            
            String latestNotification = null;
            
            @Override
            public String getIntro() {
                return "";
            }

            @Override
            public String getId() {
                String path = TerminalUtils.getPath(this);
                return path + ""
                        + "/"
                        + appName
                        + "#" + id;
            }

            @Override
            public String getDescription() {
                return latestNotification;
            }

            @Override
            public CommandResponse defaultCommand(String commandInput) {
                return reply(TerminalCode.OK);
            }

            @Override
            public String getName() {
                return appName;
            }

            @Override
            public void receiveNotification(User userSender, NotificationType notificationType, Object object) {
                latestNotification = (String) object;
            }
            
        };
        return app;
    }
    
    
    @Test
    public void helloSession() {
        
        // create the session holders
        Sessions sessions = new Sessions();
        String id = "me3";
        
        // create the users
        User user1 = UserUtils.createUserAnonymous();
        Screen screen1 = new ScreenTesting();
        TerminalApp app1 = createFakeApp(screen1, user1, "chat", id);
        Session session1 = new Session(ClientType.TELNET, app1, user1);
        sessions.addSession(session1);
        
        User user2 = UserUtils.createUserAnonymous();
        Screen screen2 = new ScreenTesting();
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
