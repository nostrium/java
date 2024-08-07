/*
 * Types of Chat
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.chat;


/**
 * @author Brito
 * @date: 2024-08-06
 * @location: Germany
 */
public enum ChatType {
    
    NORMAL,     // similar to IRC
    ANON,       // don't see identification of participants
    UNSAVED,    // don't save conversations to disk
    READ_ONLY,  // archived or only used for announcements by mods/admins
    

}
