/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package basic;

import online.nostrium.apps.basic.TerminalBasic;
import online.nostrium.apps.email.TerminalEmail;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.utils.screens.Screen;
import online.nostrium.utils.screens.ScreenLocalCLI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Brito
 * @date: 2024-09-02
 * @location: Germany
 */
public class EmailTest {

    public EmailTest() {
    }

    @Test
    public void testEmailLs() {

        User user = UserUtils.createUserAnonymous();
        assertNotNull(user);
        // needs to have a profile on disk
        user.save();

        Screen screen = new ScreenLocalCLI();
        TerminalApp app = new TerminalEmail(screen, user);
        TerminalBasic base = new TerminalBasic(screen, user);
        base.addApp(app);

        String command = "ls";
        // Handle the command request
        CommandResponse response
                = app.handleCommand(
                        TerminalType.ANSI, command);
        assertEquals(TerminalCode.OK, response.getCode());
        assertTrue(response.getText().contains("+-- inbox (0)"));
        
        System.out.println(response.getText());
        
        // cd/get inside the inbox
        command = "cd inbox";
        response = app.handleCommand(TerminalType.ANSI, command);
        
        assertEquals(TerminalCode.OK, response.getCode());
        
        assertEquals("/email/inbox", app.getId());
        System.out.println(app.getId());
        
        // send an email to yourself
        command = "email brito@nostrium.online Hello ";        
        
        // delete the user
        user.delete();
    }
}
