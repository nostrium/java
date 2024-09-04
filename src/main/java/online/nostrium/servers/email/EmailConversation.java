/*
 * Email conversation
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.email;

import com.google.gson.annotations.Expose;

/**
 * We need to somehow save email messages to disk, the problem is to connect
 * them together when people engage in conversations. This box tries to solve
 * that difficulty. It will start with one email message that sent by the user
 * and as other messages arrive, they are added.
 * 
 * The whole data is saved to disk as JSON, which permits to move it around
 * different folders and delete without difficulty when needed.
 * 
 * Each email conversation is placed on the user folder. Attachments are
 * included there also but will be placed inside a zip file to save space.
 * 
 * @author Brito
 * @date: 2024-09-02
 * @location: Germany
 */
public class EmailConversation {

    @Expose
    EmailMessage message;
    
}
