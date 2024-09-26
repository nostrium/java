/*
 * Parses Markdown text files
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.archive;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * Markdown are practical files for text that can read by humans
 * and then converted to other formats like PDF and HTML.
 * 
 * The goal here is to use for forum posts and blog posts.
 * The problem is that exists no pre-defined way to add other
 * content like comments, dates and user identification inside them.
 * 
 * @author Brito
 * @date: 2024-09-24
 * @location: Germany
 */
public class Markdown3 {

    private Topic topic;

    public Markdown3(File file) throws IOException {
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
            String authorNPUB = null;
            List<String> tags = new ArrayList<>(); // For storing tags

            Message previousMessage = null;

            // Reading lines to form the topic
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Check for header 1 for the topic title
                if (line.startsWith("# ")) {
                    title = line.substring(2).trim(); // Extract topic title
                } else if (line.startsWith("+ ")) { // Check for fields starting with '+ '
                    String[] parts = line.substring(2).trim().split(": ", 2);
                    if (parts.length < 2) continue; // Invalid line, skip
                    
                    String fieldName = parts[0].trim();
                    String fieldValue = parts[1].trim();

                    switch (fieldName) {
                        case "author":
                            author = fieldValue;
                            break;
                        case "date":
                            date = fieldValue;
                            break;
                        case "description":
                            description = fieldValue;
                            break;
                        case "tags":
                            String[] tagArray = fieldValue.split(",\\s*");
                            for (String tag : tagArray) {
                                tags.add(tag);
                            }
                            break;
                        case "content":
                            // Don't assign content here; it will be assigned later after reading the entire topic
                            break;
                    }
                } else if (line.equals("----")) {
                    // If topic is not yet created, create it
                    if (topic == null) {
                        topic = new Topic(topicContent.toString().trim(), title, author, date, description, authorNPUB, tags);
                        topicContent.setLength(0); // Reset for new messages
                    }
                } else if (line.startsWith("## ")) { // Message title
                    String currentDate = line.substring(3).trim(); // Extract date for the message

                    // If previousMessage exists, finalize the previous message
                    if (previousMessage != null) {
                        LocalDate validDate = validateDate(previousMessage.getDate(), previousMessage);
                        Message message = new Message(topicContent.toString().trim(), previousMessage.getTitle(), previousMessage.getAuthor(), validDate, previousMessage.getDescription(), previousMessage.getAuthorNPUB(), previousMessage.getTags(), topic);
                        topic.addMessage(message);
                        topicContent.setLength(0); // Reset for new message content
                    }

                    // Prepare to create a new message
                    date = currentDate; // Set the new date for the next message
                    previousMessage = new Message("", "", "", LocalDate.parse(currentDate), "", "", new ArrayList<>(tags), null); // Create a temporary message
                } else {
                    // Append line to current topic or message content
                    topicContent.append(line).append(System.lineSeparator());
                }
            }

            // Add the last message if present
            if (topicContent.length() > 0 && topic != null && previousMessage != null) {
                LocalDate validDate = validateDate(date, previousMessage);
                Message message = new Message(topicContent.toString().trim(), previousMessage.getTitle(), previousMessage.getAuthor(), validDate, previousMessage.getDescription(), previousMessage.getAuthorNPUB(), previousMessage.getTags(), topic);
                topic.addMessage(message);
            }
        }
    }

    private LocalDate validateDate(String date, Message previousMessage) {
        LocalDate validDate = null;

        // Attempt to parse the date
        try {
            validDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            // If the date is invalid, generate a random date
            if (previousMessage != null) {
                LocalDate previousDate = LocalDate.parse(previousMessage.getDate());
                LocalDate nextDate = previousMessage.getNextDate();

                // Generate a random date between previousDate and nextDate
                Random random = new Random();
                long daysBetween = nextDate.toEpochDay() - previousDate.toEpochDay();
                long randomDays = random.nextInt((int) daysBetween - 1) + 1;
                validDate = previousDate.plusDays(randomDays);
            }
        }

        return validDate;
    }

    public Topic getTopic() {
        return topic;
    }

    public void saveToFile(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("# " + topic.getTitle() + "\n\n"); // Add topic title as header 1
            writer.write("+ author: " + topic.getAuthor() + "\n");
            writer.write("+ date: " + topic.getDate() + "\n");
            writer.write("+ description: " + topic.getDescription() + "\n");
            writer.write("+ views: " + topic.getViews() + "\n");
            writer.write("+ tags: " + String.join(", ", topic.getTags()) + "\n");
            writer.write("+ content: " + topic.getContent() + "\n\n"); // Add topic content
            writer.write("----\n");
            for (Message message : topic.getMessages()) {
                writer.write("## " + message.getDate() + "\n"); // Add date as header 2
                writer.write("+ author: " + message.getAuthor() + "\n");
                writer.write("+ likes: " + message.getLikes() + "\n");
                writer.write("+ dislikes: " + message.getDislikes() + "\n");
                writer.write("+ tags: " + String.join(", ", message.getTags()) + "\n");
                writer.write("+ content: " + message.getContent() + "\n\n"); // Add message content
                writer.write("----\n");
            }
        }
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

    public static class Topic {
        private String content;
        private String title;
        private String author;
        private String date;
        private String description;
        private String authorNPUB;
        private long views;
        private final List<String> tags; // List for storing tags
        private final TreeSet<Message> messages;

        public Topic(String content, String title, String author, String date, String description, String authorNPUB, List<String> tags) {
            this.content = content; // Store content
            this.title = title;
            this.author = author;
            this.date = date != null ? date : ""; // Initialize date to empty string if null
            this.description = description;
            this.authorNPUB = authorNPUB;
            this.views = 0; // Initialize views to 0
            this.tags = tags != null ? tags : new ArrayList<>(); // Initialize tags
            this.messages = new TreeSet<>(Comparator.comparing(Message::getDate));
        }

        public void addMessage(Message message) {
            messages.add(message);
        }

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
            this.date = date != null ? date : ""; // Ensure date is not null
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAuthorNPUB() {
            return authorNPUB;
        }

        public void setAuthorNPUB(String authorNPUB) {
            this.authorNPUB = authorNPUB;
        }

        public long getViews() {
            return views;
        }

        public void setViews(long views) {
            this.views = views;
        }

        public List<String> getTags() {
            return tags;
        }

        public TreeSet<Message> getMessages() {
            return messages;
        }
    }

    public static class Message {
        private String content;
        private String title;
        private String author;
        private String date;
        private String description;
        private String authorNPUB;
        private long likes; // Counter for likes
        private long dislikes; // Counter for dislikes
        private final Topic parentTopic;
        private final List<String> tags; // List for storing tags

        public Message(String content, String title, String author, LocalDate date, String description, String authorNPUB, List<String> tags, Topic parentTopic) {
            this.content = content; // Content of the message
            this.title = title;
            this.author = author;
            this.date = date != null ? date.toString() : ""; // Ensure date is not null
            this.description = description;
            this.authorNPUB = authorNPUB;
            this.likes = 0; // Initialize likes to 0
            this.dislikes = 0; // Initialize dislikes to 0
            this.tags = tags != null ? tags : new ArrayList<>(); // Initialize tags
            this.parentTopic = parentTopic;
        }

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
            this.date = date != null ? date : ""; // Ensure date is not null
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAuthorNPUB() {
            return authorNPUB;
        }

        public void setAuthorNPUB(String authorNPUB) {
            this.authorNPUB = authorNPUB;
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

        public LocalDate getNextDate() {
            // Retrieve the next message's date if it exists
            if (parentTopic != null) {
                Message nextMessage = parentTopic.getMessages().higher(this);
                if (nextMessage != null) {
                    return LocalDate.parse(nextMessage.getDate());
                }
            }
            return LocalDate.MAX; // Return max date if no next message exists
        }

        @Override
        public String toString() {
            return "Message{" +
                    "content='" + content + '\'' +
                    ", title='" + title + '\'' +
                    ", author='" + author + '\'' +
                    ", date='" + date + '\'' +
                    ", description='" + description + '\'' +
                    ", authorNPUB='" + authorNPUB + '\'' +
                    ", likes=" + likes +
                    ", dislikes=" + dislikes +
                    ", tags=" + String.join(", ", tags) +
                    '}';
        }
    }
}
