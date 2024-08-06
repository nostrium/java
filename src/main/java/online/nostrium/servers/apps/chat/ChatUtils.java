/*
 * Internal utils
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.chat;


import online.nostrium.users.*;
import static online.nostrium.utils.NostrUtils.generateNostrKeys;
import online.nostrium.utils.TextFunctions;


/**
 * @author Brito
 * @date: 2024-08-06
 * @location: Germany
 */
public class ChatUtils {
    
    /**
     * Create an chat room without saving it to disk
     *
     * @param user
     * @return a fresh chat room without custom data
     */
    public static ChatRoom createChatRoom(User user) {
        // generate random nostr keys
        String[] keys = generateNostrKeys();
        ChatRoom chatRoom = new ChatRoom(keys[1]);
        chatRoom.setNsec(keys[0]);
        
        // add the user as founder
        chatRoom.addAdmin(user.getNpub());
        
        // set the name
        chatRoom.setName("randochat#" 
                + chatRoom.getNpub().substring(0, 4)
        );
        
        // set the registration time
        String timestamp = TextFunctions.getDate();
        chatRoom.setRegisteredTime(timestamp);
        
        // all done
        return chatRoom;
    }

}
