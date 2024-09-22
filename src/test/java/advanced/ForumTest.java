/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package advanced;

import online.nostrium.forum.Forum;
import online.nostrium.forum.Group;
import online.nostrium.forum.Message;
import online.nostrium.forum.Topic;
import online.nostrium.utils.TextFunctions;
import online.nostrium.utils.time;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Date: 2024-09-22
 * Place: Germany
 * @author brito
 */
public class ForumTest {
    
    public ForumTest() {
    }

    @Test
    public void helloForum() {
        
        Forum forum = new Forum("offgrid");
        Group group1 = new Group("general");
        forum.addGroup(group1);
        assertEquals(1, forum.getGroups().size());
        
        // create a new topic
        Message message = new Message(time.getTimeISO(), "Testing how messages get written");
        // start the topic with an opening message
        Topic topic = new Topic("1234", "Hello World", message);
        // add this topic to the group
        group1.addTopic(topic);
        
        assertEquals(1, group1.getTopics().size());
        assertEquals("Hello World", topic.getTitle());
        
    }
}
