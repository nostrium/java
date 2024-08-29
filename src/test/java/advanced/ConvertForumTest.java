/*
 *  Convert an online forum to nostrium format
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package advanced;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.main.Folder;
import online.nostrium.main.old.forum.convert.ConvertIPB;
import online.nostrium.main.old.forum.structures.ForumGroup;
import online.nostrium.main.old.forum.structures.ForumTopic;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

/**
 * Date: 2023-02-09
 * Place: Germany
 * @author brito
 */
public class ConvertForumTest {
    
    public ConvertForumTest() {
    }

     /*
    
    @Test
    public void convertForumTopicTest() {
    
       
        Folder.deleteTestFolder();
        
        String url = "http://reboot.pro";
        String id = "22277";
        
        ForumTopic forumTopic = new ForumTopic();
        forumTopic.setId(id);
        
        ConvertIPB.extractContentFromTopic(forumTopic, url);
        
        File file = new File(Folder.getFolderTest(), forumTopic.getFilename() + ".json");
        try {
            FileUtils.writeStringToFile(file, forumTopic.jsonExport());
            System.out.println("Wrote: " + file.getPath());
        } catch (IOException ex) {
            Logger.getLogger(ConvertForumTest.class.getName()).log(Level.SEVERE, null, ex);
        }
   
    }
    
    
    @Test
    public void convertForumGroupTest() {
    
        String url = "http://reboot.pro";
        String id = "reboot.pro";
        ConvertIPB convert = new ConvertIPB(id, url);
        
        ForumGroup group = new ForumGroup();
        group.setId("71");
        group.setTitle("test");
        
        convert.getForum().addForumGroup(group);
        convert.extractPostsFromForumGroup(group);
        
    }
    

    @Test
    public void convertForumTest() {
    
        String url = "http://reboot.pro";
        String id = "reboot.pro";
        ConvertIPB convert = new ConvertIPB(id, url);
        convert.start();
    }
        
*/
     
}
