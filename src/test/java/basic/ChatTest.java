/*
 *  Test chat creation/management
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package basic;

import java.io.File;
import online.nostrium.servers.apps.chat.ChatRoom;
import online.nostrium.servers.apps.chat.ChatType;
import online.nostrium.servers.apps.chat.ChatUtils;
import online.nostrium.users.User;
import online.nostrium.users.UserUtils;
import online.nostrium.utils.TextFunctions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Date: 2024-08-06
 * Place: Germany
 * @author brito
 */
public class ChatTest {
    
    public ChatTest() {
    }

    @Test
    public void helloChat() {
        // create the owner of the chat
        User user = UserUtils.createUserAnonymous();
        
        // create an empty room
        ChatRoom room = ChatUtils.createChatRoom(user);
        
        assertNotNull(user);
        assertNotNull(room);
        assertNotNull(room.getNsec());
        assertNotNull(room.getName());
        assertEquals(ChatType.NORMAL, room.getChatType());
        String timestamp = TextFunctions.getDate();
        assertEquals(user.getRegisteredTime(), timestamp);
        
        room.save();
        File file = room.getFile();
        assertTrue(file.exists());
        
        // now delete the file
        
    }
}
