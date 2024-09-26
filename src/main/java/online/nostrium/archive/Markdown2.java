package online.nostrium.archive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses Markdown text files
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 *
 * Markdown2 parses a Markdown text file containing a topic and multiple messages.
 */
public class Markdown2 {
    private Topic topic;

    public Markdown2(File file) throws IOException {
        parseMarkdownFile(file);
    }

    private void parseMarkdownFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder topicContent = new StringBuilder();
            String title = null;
            String author = null;
            String date = null;
            String description = null;
            List<String> tags = new ArrayList<>(); // For storing tags
            Message currentMessage = null; // Current message being processed
            List<Message> messages = new ArrayList<>(); // List of messages

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Parse topic title
                if (line.startsWith("# ")) {
                    title = line.substring(2).trim();
                } else if (line.startsWith("+ ")) { // Fields starting with '+ '
                    String[] parts = line.substring(2).trim().split(": ", 2);
                    if (parts.length < 2) continue;

                    String fieldName = parts[0].trim();
                    String fieldValue = parts[1].trim();

                    switch (fieldName) {
                        case "author":
                            author = fieldValue; // Topic author
                            break;
                        case "date":
                            date = fieldValue; // Topic date
                            break;
                        case "description":
                            description = fieldValue; // Topic description
                            break;
                        case "tags":
                            String[] tagArray = fieldValue.split(",\\s*");
                            for (String tag : tagArray) {
                                tags.add(tag);
                            }
                            break;
                        case "content":
                            topicContent.append(fieldValue).append(System.lineSeparator());
                            break;
                    }
                } else if (line.equals("----")) {
                    // If currentMessage exists, finalize it
                    if (currentMessage != null) {
                        messages.add(currentMessage);
                        currentMessage = null; // Reset for next message
                    } else if (topic == null) { // Only create topic if it hasn't been created
                        topic = new Topic(title, author, date, description, tags, topicContent.toString().trim());
                        topicContent.setLength(0); // Clear for next message
                    }
                } else if (line.startsWith("## ")) { // Message header
                    if (currentMessage != null) { // Finalize the previous message
                        messages.add(currentMessage);
                    }

                    String messageDate = line.substring(3).trim();
                    currentMessage = new Message("", messageDate, "", new ArrayList<>(), false, ""); // Initialize new message
                } else if (currentMessage != null) {
                    // Append message content
                    if (line.startsWith("+ ")) {
                        String[] parts = line.substring(2).trim().split(": ", 2);
                        if (parts.length < 2) continue;

                        String fieldName = parts[0].trim();
                        String fieldValue = parts[1].trim();

                        switch (fieldName) {
                            case "author":
                                currentMessage.setAuthor(fieldValue); // Set message author
                                break;
                            case "likes":
                                currentMessage.setLikes(Integer.parseInt(fieldValue)); // Set message likes
                                break;
                            case "dislikes":
                                currentMessage.setDislikes(Integer.parseInt(fieldValue)); // Set message dislikes
                                break;
                            case "tags":
                                String[] messageTags = fieldValue.split(",\\s*");
                                for (String tag : messageTags) {
                                    currentMessage.getTags().add(tag); // Set message tags
                                }
                                break;
                            case "content":
                                currentMessage.setContent(fieldValue); // Set message content
                                break;
                        }
                    }
                }
            }

            // Finalize the last message if it exists
            if (currentMessage != null) {
                messages.add(currentMessage);
            }

            if (topic != null) {
                topic.setMessages(messages);
            }
        }
    }

    public Topic getTopic() {
        return topic;
    }

    public static class Topic {
        private String title;
        private String author;
        private String date;
        private String description;
        private List<String> tags;
        private String content;
        private List<Message> messages;

        public Topic(String title, String author, String date, String description, List<String> tags, String content) {
            this.title = title;
            this.author = author;
            this.date = date;
            this.description = description;
            this.tags = tags != null ? tags : new ArrayList<>();
            this.content = content;
            this.messages = new ArrayList<>();
        }

        public void setMessages(List<Message> messages) {
            this.messages = messages;
        }

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public List<String> getTags() { return tags; }
        public String getContent() { return content; }
        public List<Message> getMessages() { return messages; }
    }

    public static class Message {
        private String author;
        private String date;
        private int likes;
        private int dislikes;
        private List<String> tags;
        private boolean edited;
        private String content;

        public Message(String author, String date, String content, List<String> tags, boolean edited, String description) {
            this.author = author;
            this.date = date;
            this.content = content;
            this.tags = tags != null ? tags : new ArrayList<>();
            this.edited = edited;
        }

        // Getters and Setters
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public int getLikes() { return likes; }
        public void setLikes(int likes) { this.likes = likes; }
        public int getDislikes() { return dislikes; }
        public void setDislikes(int dislikes) { this.dislikes = dislikes; }
        public List<String> getTags() { return tags; }
        public boolean isEdited() { return edited; }
        public void setEdited(boolean edited) { this.edited = edited; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}
