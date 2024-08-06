/*
 *  Represents a note
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.forum.structures;

/**
 * Date: 2023-02-10
 * Place: Germany
 * @author brito
 */
public class ForumNote {

    String id;
    String timestamp;
    String author;
    String content;
    String replyTo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }
    
}
