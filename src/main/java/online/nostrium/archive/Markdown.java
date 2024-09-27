/*
 * Parses Markdown text files
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.archive;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import online.nostrium.utils.time;
import org.apache.commons.io.FileUtils;

/**
 * Markdown are practical files for text that can read by humans and then
 * converted to other formats like PDF and HTML.
 *
 * The goal here is to use for forum posts and blog posts. The problem is that
 * exists no pre-defined way to add other content like comments, dates and user
 * identification inside them.
 *
 * @author Brito
 * @date: 2024-09-24
 * @location: Germany
 */
public class Markdown {

    private Topic topic = new Topic();
    
    public Markdown(){
    }

    public Markdown(File file) throws IOException {

        // read the text file
        String textFile = FileUtils.readFileToString(file, Charset.defaultCharset());

        // clean up the file
        textFile = textFile.replace("\r\n", "\n");

        // get the blocks with data
        String[] blocks = textFile.split("----");

        if (blocks.length == 0) {
            return;
        }

        // parse the header topic
        parseTopic(blocks[0], topic);
        
        for(int i = 1; i < blocks.length; i++){
            parseMessage(blocks[i]);
        }

    }

    private void parseTopic(String block, Topic topic) {
        String[] lines = block.split("\n");
        boolean canAddContent = false;
        boolean fieldsHaveFinished = false;
        for (String line : lines) {
            
            // empty line was detected after the fields
            if(line.isBlank() && canAddContent){
                fieldsHaveFinished = true;
                continue;
            }
            
            if(fieldsHaveFinished && canAddContent){
                topic.setContent(topic.getContent() + "\n" + line);
            }

            if (line.startsWith("# ") && fieldsHaveFinished == false) {
                line = line.trim();
                topic.setTitle(line.substring(2).trim()); // Extract topic title
                continue;
            }
            
            if (line.startsWith("> ") && fieldsHaveFinished == false) {
                line = line.trim();
                topic.setDescription(line.substring(2).trim()); // Extract topic title
                continue;
            }

            if (line.startsWith("+ ") && fieldsHaveFinished == false) { // Check for fields starting with '+ '
                line = line.trim();
                String[] parts = line.substring(2).trim().split(": ", 2);
                if (parts.length < 2) {
                    continue; // Invalid line, skip
                }
                // after an empty line, start adding content
                canAddContent = true;

                String fieldName = parts[0].trim();
                String fieldValue = parts[1].trim();

                switch (fieldName) {
                    case "author":
                        topic.setAuthor(fieldValue);
                        break;
                    case "title":
                        topic.setTitle(fieldValue);
                        break;
                    case "npub":
                        topic.setNpub(fieldValue);
                        break;
                    case "date":
                        topic.setDate(fieldValue);
                        break;
                    case "description":
                        topic.setDescription(fieldValue);
                        break;
                    case "likes":
                        topic.setLikes(Long.parseLong(fieldValue));
                        break;
                    case "dislikes":
                        topic.setDislikes(Long.parseLong(fieldValue));
                        break;
                    case "tags":
                        String[] tagArray = fieldValue.split(",\\s*");
                        for (String tag : tagArray) {
                            topic.getTags().add(tag);
                        }
                        break;
                }
            }
        }
        // trim the content
        String content = topic.getContent().trim();
        topic.setContent(content);
    }
    
    private void parseMessage(String block) {
        Message message = new Message();
        String[] lines = block.split("\n");
        boolean canAddContent = false;
        boolean fieldsHaveFinished = false;
        for (String line : lines) {
            
            // empty line was detected after the fields
            if(line.isBlank() && canAddContent){
                fieldsHaveFinished = true;
                continue;
            }
            
            if(fieldsHaveFinished && canAddContent){
                message.setContent(message.getContent() + "\n" + line);
            }

            if (line.startsWith("## ")) {
                line = line.trim();
                message.setDate(line.substring(3).trim()); // Extract topic title
                continue;
            }

            if (line.startsWith("+ ")) { // Check for fields starting with '+ '
                line = line.trim();
                String[] parts = line.substring(2).trim().split(": ", 2);
                if (parts.length < 2) {
                    continue; // Invalid line, skip
                }
                // after an empty line, start adding content
                canAddContent = true;

                String fieldName = parts[0].trim();
                String fieldValue = parts[1].trim();

                switch (fieldName) {
                    case "author":
                        message.setAuthor(fieldValue);
                        break;
                    case "title":
                        message.setTitle(fieldValue);
                        break;
                    case "npub":
                        message.setNpub(fieldValue);
                        break;
                    case "date":
                        message.setDate(fieldValue);
                        break;
                    case "description":
                        message.setDescription(fieldValue);
                        break;
                    case "likes":
                        message.setLikes(Long.parseLong(fieldValue));
                        break;
                    case "dislikes":
                        message.setDislikes(Long.parseLong(fieldValue));
                        break;
                    case "tags":
                        String[] tagArray = fieldValue.split(",\\s*");
                        for (String tag : tagArray) {
                            message.getTags().add(tag);
                        }
                        break;
                }
            }
        }
        // trim the content
        String content = message.getContent().trim();
        message.setContent(content);
        // add the message
        topic.getMessages().add(message);
    }
    

    public Topic getTopic() {
        return topic;
    }

    public void saveToFile(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // write the topic
            writer.write("# " + topic.getTitle() + "\n"); // Add topic title as header 1
            writeIfNotEmpty(writer, "> ", topic.getDescription(), "\n\n");
            
            writeIfNotEmpty(writer, "+ author: ", topic.getAuthor(), "\n");
            writeIfNotEmpty(writer, "+ npub: ", topic.getNpub(), "\n");
            writeIfNotEmpty(writer, "+ date: ", topic.getDate(), "\n");
            String tags = String.join(", ", topic.getTags());
            writeIfNotEmpty(writer, "+ tags: ", tags, "\n");
            writeIfNotEmpty(writer, "+ views: ", topic.getViews(), "\n");
            writeIfNotEmpty(writer, "+ like: ", topic.getLikes(), "\n");
            writeIfNotEmpty(writer, "+ dislikes: ", topic.getDislikes(), "\n");
            writeIfTrue(writer, "+ edited: ", topic.isEdited(), "\n");
                // write the content
            writeIfNotEmpty(writer, "\n", topic.getContent(), "");
            // write the separator
            //writer.write("----\n");
            
            // write the messages
            for (Message message : topic.getMessages()) {
                // write the separator
                writer.write("\n\n----\n\n");
                
                writer.write("## " + message.getDate() + "\n"); // Add date as header 2
                writeIfNotEmpty(writer, "> ", message.getDescription(), "\n");
                
                writeIfNotEmpty(writer, "+ title: ", message.getTitle(), "\n");
                writeIfNotEmpty(writer, "+ author: ", message.getAuthor(), "\n");
                writeIfNotEmpty(writer, "+ npub: ", message.getNpub(), "\n");
                writeIfNotEmpty(writer, "+ date: ", message.getDate(), "\n");
                writeIfNotEmpty(writer, "+ date: ", message.getDate(), "\n");
                writeIfNotEmpty(writer, "+ like: ", message.getLikes(), "\n");
                writeIfNotEmpty(writer, "+ dislikes: ", message.getDislikes(), "\n");
                writeIfTrue(writer, "+ edited: ", message.isEdited(), "\n");
                
                tags = String.join(", ", message.getTags());
                writeIfNotEmpty(writer, "+ tags: ", tags, "\n");
                // write the content
                writeIfNotEmpty(writer, "\n", message.getContent(), "");
                
            }
        }
    }
    
    private void writeIfNotEmpty(BufferedWriter writer, String t1, long value, String t2) throws IOException {
        if(value == 0){
            return;
        }
        writeIfNotEmpty(writer, t1, value + "", t2);
    }
    
    private void writeIfTrue(BufferedWriter writer, String t1, boolean value, String t2) throws IOException {
        if(value == false){
            return;
        }
        writeIfNotEmpty(writer, t1, value + "", t2);
    }
     
    
    
    private void writeIfNotEmpty(BufferedWriter writer, String t1, String text, String t2) throws IOException {
        if(text == null || text.isEmpty()){
            return;
        }
        writer.write(t1 + text + t2);
    }

    public boolean validateMinimumFields() {
        if (topic == null) {
            return false;
        }
        boolean isValid = !topic.getTitle().isEmpty() && !topic.getAuthor().isEmpty()
                && !topic.getDate().isEmpty() && !topic.getContent().isEmpty();

        for (Message message : topic.getMessages()) {
            isValid &= !message.getAuthor().isEmpty() && !message.getDate().isEmpty() && !message.getContent().isEmpty();
        }
        return isValid;
    }


    public static class Topic extends Message {

        private long views;
        private final TreeSet<Message> messages;

        public Topic() {
            this.views = 0; // Initialize views to 0
            this.messages = new TreeSet<>(Comparator.comparing(Message::getDate));
        }

        public void addMessage(Message message) {
            messages.add(message);
        }

        public long getViews() {
            return views;
        }

        public void setViews(long views) {
            this.views = views;
        }

        public TreeSet<Message> getMessages() {
            return messages;
        }

        public void setDate() {
            String date = time.getCurrentDay();
            setDate(date);
        }
    }

    public static class Message {

        private String content = "";
        private String title = "";
        private String author = "";
        private String date = "";
        private String description = "";
        private String npub = "";
        private long likes = 0; // Counter for likes
        private long dislikes = 0; // Counter for dislikes
        private boolean edited = false;
        private final List<String> tags = new ArrayList<>(); // List for storing tags

        
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isEdited() {
            return edited;
        }

        public void setEdited(boolean edited) {
            this.edited = edited;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getNpub() {
            return npub;
        }

        public void setNpub(String npub) {
            this.npub = npub;
        }

        public long getLikes() {
            return likes;
        }

        public void setLikes(long likes) {
            this.likes = likes;
        }

        public long getDislikes() {
            return dislikes;
        }

        public void setDislikes(long dislikes) {
            this.dislikes = dislikes;
        }

        public List<String> getTags() {
            return tags;
        }

        @Override
        public String toString() {
            return "Message{"
                    + "content='" + content + '\''
                    + ", title='" + title + '\''
                    + ", author='" + author + '\''
                    + ", date='" + date + '\''
                    + ", description='" + description + '\''
                    + ", npub='" + npub + '\''
                    + ", likes=" + likes
                    + ", dislikes=" + dislikes
                    + ", tags=" + String.join(", ", tags)
                    + '}';
        }
    }
}
