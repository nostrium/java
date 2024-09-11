/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package basic;

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

        String command = "ls";
        // Handle the command request
        CommandResponse response
                = app.handleCommand(
                        TerminalType.ANSI, command);
        assertEquals(TerminalCode.OK, response.getCode());

        System.out.println(response.getText());
        
        
        // delete the user
        user.delete();
    }
}
