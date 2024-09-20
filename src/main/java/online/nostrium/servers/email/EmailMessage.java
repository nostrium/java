/*
 * Defines an email message
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.email;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import online.nostrium.user.User;

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
    long timeReceived = -1,     // when was it received?
            timeCreated = -1;   // when was it created?
    

    @Expose
    EmailMessage messageParent = null; // is it part of a conversation?

    @Expose
    ArrayList<EmailMessage>
            messageReplies = new ArrayList<>(); // which answers were made?

    public EmailMessage() {
        timeCreated = System.currentTimeMillis();
    }

    
    
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    
    public void setSender(User user) {
        this.sender = user.getEmailAddress();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ArrayList<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(ArrayList<String> receivers) {
        this.receivers = receivers;
    }
    
    public void addReceiver(User user) {
        String emailAddress = user.getEmailAddress();
        addReceiver(emailAddress);
    }
    
    public void addReceiver(String receiver) {
        if(receivers.contains(receiver)){
            return;
        }
        receivers.add(receiver);
    }

    public ArrayList<String> getHeaders() {
        return headers;
    }

    public void setHeaders(ArrayList<String> headers) {
        this.headers = headers;
    }

    public ArrayList<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<String> attachments) {
        this.attachments = attachments;
    }

    public boolean isReadByReceiver() {
        return readByReceiver;
    }

    public void setReadByReceiver(boolean readByReceiver) {
        this.readByReceiver = readByReceiver;
    }

    public long getTimeReceived() {
        return timeReceived;
    }

    public void setTimeReceived(long timeReceived) {
        this.timeReceived = timeReceived;
    }

    public EmailMessage getMessageParent() {
        return messageParent;
    }

    public void setMessageParent(EmailMessage messageParent) {
        this.messageParent = messageParent;
    }

    public ArrayList<EmailMessage> getMessageReplies() {
        return messageReplies;
    }

    public void setMessageReplies(ArrayList<EmailMessage> messageReplies) {
        this.messageReplies = messageReplies;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }



    
}
