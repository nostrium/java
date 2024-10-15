/*
 * Defines an email message
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.email;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.HashMap;
import online.nostrium.user.User;

/**
 * @author Brito
 * @date: 2024-09-02
 * @location: Germany
 */
public class EmailMessage {

    @Expose
    String 
        from = null,      // who is sending this email
        title = null,       // what is the title
        body = null;        // what is the content of the message

    @Expose
    ArrayList<String> 
            toUsers = new ArrayList<>(),              // who is receiving?
            attachments = new ArrayList<>();            // which files attached?

    
    @Expose
    boolean readByReceiver = false; // was this read by the intended reader?

    @Expose
    long timeReceived = -1,     // when was it received?
            timeCreated = -1;   // when was it created?
    
    @Expose
    HashMap<String, String> headers = new HashMap<>();
    
    @Expose
    EmailMessage messageParent = null; // is it part of a conversation?

    @Expose
    ArrayList<EmailMessage>
            messageReplies = new ArrayList<>(); // which answers were made?

    public EmailMessage() {
        timeCreated = System.currentTimeMillis();
    }

    
    
    public String getFrom() {
        return from;
    }

    public void setFrom(String sender) {
        this.from = sender;
    }
    
    public void setFrom(User user) {
        this.from = user.getEmailAddress();
    }

    public String getTitle() {
        return title;
    }
    
     public String getTitleNeverNull() {
        if(title == null || title.isEmpty()){
            return "(No title)";
        }
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

    public String getToUsersSingleLine() {
        String recipients = String.join(",", toUsers);
        return recipients;
    }
    
    public ArrayList<String> getToUsers() {
        return toUsers;
    }

    public void setToUsers(ArrayList<String> receivers) {
        this.toUsers = receivers;
    }
    
    public void addReceiver(User user) {
        String emailAddress = user.getEmailAddress();
        addToUser(emailAddress);
    }
    
    public void addToUser(String receiver) {
        if(toUsers.contains(receiver)){
            return;
        }
        toUsers.add(receiver);
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
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

    /**
     * Export this object as JSON
     *
     * @return null if something went wrong
     */
    public String jsonExport() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                //.enableComplexMapKeySerialization()
                //.setLenient()
                .setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }

    
}
