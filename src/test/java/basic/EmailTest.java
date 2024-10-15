/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package basic;

import com.icegreen.greenmail.util.ServerSetup;
import static com.icegreen.greenmail.util.ServerSetup.PROTOCOL_SMTP;
import java.io.File;
import online.nostrium.main.core;
import online.nostrium.servers.email.EmailMessage;
import online.nostrium.servers.email.EmailUtils;
import static online.nostrium.servers.email.EmailUtils.getFolderEmail;
import online.nostrium.servers.email.ServerEmail;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;
import online.nostrium.utils.time;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * @author Brito
 * @date: 2024-09-02
 * @location: Germany
 */
public class EmailTest {

    public EmailTest() {
    }

    @Test
    public void testEmailReceive(){
        // start the config
        core.startConfig();
        
        // start the email server
        ServerEmail server = new ServerEmail();
        server.start();
        
        User user1 = UserUtils.createUserAnonymous();
        user1.save();
        
        ServerSetup serverSMTP = 
                new ServerSetup(2500, "127.0.0.1", PROTOCOL_SMTP);
        
        
        String mailTo = user1.getNpub() + "@" + core.config.getDomain();
        String mailFrom = "brito@localhost";
        
        EmailMessage msg = new EmailMessage();
        msg.setFrom(mailFrom);
        msg.addToUser(mailTo);
        msg.setTitle("Hello Title!");
        msg.setBody("Hello there, this is the text body");
        
        // get the inbox folder
        File folderInbox = getFolderEmail(user1, "inbox");
        assertTrue(folderInbox.exists());
        
        EmailUtils.sendEmail(msg, serverSMTP);
        
        time.wait(3);
        
        assertTrue(folderInbox.exists());
        
        File[] files = folderInbox.listFiles();
        assertEquals(1, files.length);
        
        
        // delete all files
        user1.delete();
    }
    
    
//    @Test
//    public void testEmailLs() {
//        User user = UserUtils.createUserAnonymous();
//        assertNotNull(user);
//        // needs to have a profile on disk
//        user.save();
//
//        Screen screen = new ScreenCLI();
//        TerminalApp app = new TerminalEmail(screen, user);
//        TerminalBasic base = new TerminalBasic(screen, user);
//        base.addApp(app);
//
//        String command = "ls";
//        // Handle the command request
//        CommandResponse response
//                = app.handleCommand(
//                        TerminalType.ANSI, command);
//        assertEquals(TerminalCode.OK, response.getCode());
//        assertTrue(response.getText().contains("+-- inbox (0)"));
//        
//        System.out.println(response.getText());
//        
//        // cd/get inside the inbox
//        command = "cd inbox";
//        response = app.handleCommand(TerminalType.ANSI, command);
//        
//        assertEquals(TerminalCode.OK, response.getCode());
//        
//        assertEquals("/email/inbox", app.getId());
//        System.out.println(app.getId());
//        
//        // send an email to yourself
//        command = "email brito@nostrium.online Hello ";        
//        
//        // delete the user
//        user.delete();
//    }
//    


}
