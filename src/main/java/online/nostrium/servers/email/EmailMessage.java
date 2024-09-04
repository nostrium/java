/*
 * Defines an email message
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.email;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;

/**
 * @author Brito
 * @date: 2024-09-02
 * @location: Germany
 */
public class EmailMessage {

    @Expose
    String 
        sender = null,      // who is sending this email
        title = null,       // what is the title
        body = null;        // what is the content of the message

    @Expose
    ArrayList<String> 
            receivers = new ArrayList<>(),              // who is receiving?
            headers = new ArrayList<>(),                // forensics
            attachments = new ArrayList<>();            // which files attached?

    @Expose
    boolean readByReceiver = false; // was this read by the intended reader?

    @Expose
    long timeReceived;  // when was it received?

    @Expose
    EmailMessage messageParent = null; // is it part of a conversation?

    @Expose
    ArrayList<EmailMessage>
            messageReplies = new ArrayList<>(); // which answers were made?

}
