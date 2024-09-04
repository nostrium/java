/*
 * <title>
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.utils.events;

/**
 * @author Brito
 * @date: 2 Sept 2024
 * @location: Germany
 */
import java.util.LinkedList;

public class MessageQueue {
    private final LinkedList<String> messages;
    private final int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
        this.messages = new LinkedList<>();
    }

    public void addMessage(String message) {
        if (messages.size() == capacity) {
            messages.removeFirst();  // Remove the oldest message
        }
        messages.addLast(message);  // Add the new message
    }

    public LinkedList<String> getMessages() {
        return new LinkedList<>(messages);  // Return a copy to prevent modification
    }

    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(10);

        for (int i = 1; i <= 12; i++) {
            queue.addMessage("Message " + i);
        }

        System.out.println(queue.getMessages());  // Output: [Message 3, Message 4, ..., Message 12]
    }
}
