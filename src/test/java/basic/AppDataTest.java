/*
 *  Test storing and reading data
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package basic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.apps.AppData;
import online.nostrium.apps.games.guess_number.UserRecordGuess;
import online.nostrium.apps.user.User;
import online.nostrium.apps.user.UserUtils;
import online.nostrium.notifications.ClientType;
import online.nostrium.notifications.Session;
import online.nostrium.notifications.Sessions;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.screens.Screen;
import online.nostrium.servers.terminal.screens.ScreenTesting;
import static online.nostrium.utils.TestingFunctions.createFakeApp;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Date: 2024-08-25
 * Place: Germany
 * @author brito
 */
public class AppDataTest {
    
    public AppDataTest() {
    }

   
    @Test
    public void helloData() {
        
        // create the session holders
        Sessions sessions = new Sessions();
        String id = "test";
        
        // create the users
        User user1 = UserUtils.createUserAnonymous();
        Screen screen1 = new ScreenTesting();
        TerminalApp app1 = createFakeApp(screen1, user1, id, id);
        Session session1 = new Session(ClientType.TELNET, app1, user1);
        sessions.addSession(session1);
        
        // start testing
        File file = app1.data.getFile();
        File folder = file.getParentFile();
        try {
            FileUtils.deleteDirectory(folder);
        } catch (IOException ex) {
            Logger.getLogger(AppDataTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // make sure that folder does not exist
        assertFalse(folder.exists());
        
        // delete previous data
        app1.data.delete();
               
        
        // save some values
        app1.data.put("tag1", "value1");
        app1.data.put("tag2", "value2");
        assertEquals(2, app1.data.getData().size());
        
        String test1 = (String) app1.data.get("tag1");
        assertEquals("value1", test1);
        
        // save to disk
        app1.data.save();
        
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
        
        AppData appData = new AppData(app1);
        
        HashMap<String, Object> data = appData.getData();
        assertEquals(2, data.size());
        
        // now add a more complex object
        @SuppressWarnings("unchecked")
        ArrayList<String> list = new ArrayList();
        list.add("1");
        list.add("2");
        
        appData.put("list", list);
        
        assertTrue(appData.has("list"));
        
        @SuppressWarnings("unchecked")
        ArrayList<String> test2 = (ArrayList<String>) appData.get("list");
        assertEquals(2, test2.size());
        
        // save to disk and see if it worked
        appData.save();
        
        // load as a new appData
        appData = new AppData(app1);
        
        @SuppressWarnings("unchecked")
        ArrayList<String> test3 = (ArrayList<String>) appData.get("list");
        assertEquals(2, test3.size());
        
        
        // move to place an ArrayList with objects
        @SuppressWarnings("unchecked")
        ArrayList<UserRecordGuess> listScore1 = new ArrayList();
        UserRecordGuess score1 = new UserRecordGuess(1, "x", 1, 1);
        UserRecordGuess score2 = new UserRecordGuess(2, "y", 2, 2);
        listScore1.add(score1);
        listScore1.add(score2);
        
        appData.put("score", listScore1);
        appData.save();
        
        // load as a new appData
        appData = new AppData(app1);
        @SuppressWarnings("unchecked")
        ArrayList<UserRecordGuess> listScore2 = (ArrayList<UserRecordGuess>) appData.get("score");
        assertEquals(2, listScore2.size());
        
        // delete previous data
        app1.data.delete();
               
    }
}
