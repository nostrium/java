/*
 *  Functions related unit testing
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.utils;

import online.nostrium.user.User;
import online.nostrium.session.NotificationType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.session.Session;

/**
 * @Date: 2024-08-25
 * @Place: Germany
 * @author brito
 */
public class TestingFunctions {

    public static TerminalApp createFakeApp(Session session, String appName, String id){
        
        TerminalApp app = new TerminalApp(session) {
            
            String latestNotification = null;
            
            @Override
            public String getIntro() {
                return "";
            }

            @Override
            public String getId() {
                String path = "root";
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
            public String getPathWithName() {
                return appName;
            }

            @Override
            public void receiveNotification(User userSender, NotificationType notificationType, Object object) {
                latestNotification = (String) object;
            }
            
        };
        return app;
    }
    
}
