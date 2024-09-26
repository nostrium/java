/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package advanced;

import java.io.File;
import java.io.IOException;
import online.nostrium.archive.ArchiveType;
import online.nostrium.archive.ArchiveUtils;
import static online.nostrium.archive.ArchiveUtils.generateFilename;
import online.nostrium.archive.Group;
import online.nostrium.archive.Message;
import online.nostrium.archive.Topic;
import online.nostrium.archive.forum.ForumArchive;
import online.nostrium.main.Folder;
import online.nostrium.main.core;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;
import online.nostrium.utils.screens.Screen;
import online.nostrium.utils.screens.ScreenCLI;
import online.nostrium.utils.time;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Date: 2024-09-22 Place: Germany
 *
 * @author brito
 */
public class ArchiveTest {

    public ArchiveTest() {
    }

    @Test
    public void helloForum() throws IOException {
        
        // save the data to disk
        File folder = Folder.getFolderTest();
        assertTrue(folder.exists());
        
        Screen screen = new ScreenCLI();
        User user = UserUtils.createUserAnonymous();
        core.startConfig();
        
        ForumArchive forum = new ForumArchive("offgrid", folder, screen, user);
        
        File folderGeneral = new File(folder, "general");
        Group group1 = new Group(folderGeneral);
        group1.setTitle("General 1");
        group1.setDescription("Testing a group");
        forum.addGroup(group1);
        assertEquals(1, forum.getGroups().size());
        
        assertTrue(group1.getFile().exists());

        // create a new topic
        Message message = new Message(time.getTimeISO(), "Testing how messages get written");
        // start the topic with an opening message
        Topic topic = new Topic("1234", "Hello World", message, group1.folder);

        assertEquals(1, group1.getTopics().size());
        assertEquals("Hello World", topic.getTitle());

        // get the result
        String text = forum.jsonExport();
        // the forum only puts strings
        // otherwise it would contain all the data and blow the available memory
        
        assertTrue(text.contains("\"id\": \"offgrid\""));
        
        forum.save();
        assertTrue(forum.getFile().exists());

        
        // get inside the folder
        
        
        System.gc();
        // delete all files
        FileUtils.deleteDirectory(folder);
    }
    
    @Test
    public void helloArchiveUtils(){
    
        String title = "Sample Blog Post: Understanding Java! @2024";
        String filename = generateFilename(title);
        
        
        System.out.println(filename);
        
    }
}