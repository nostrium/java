/*
 * Nostr Event 01
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.nostr.relays;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.List;


/**
 * @author Brito
 * @date: 2024-10-17
 * @location: Portugal
 */
public class NostrEvent {
@SerializedName("content")
    private String content;

    @SerializedName("created_at")
    private long createdAt;

    @SerializedName("id")
    private String id;

    @SerializedName("kind")
    private int kind;

    @SerializedName("pubkey")
    private String pubkey;

    @SerializedName("sig")
    private String sig;

    @SerializedName("tags")
    private List<List<String>> tags;

    // Getters for each field
    public String getContent() {
        return content;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public int getKind() {
        return kind;
    }

    public String getPubkey() {
        return pubkey;
    }

    public String getSig() {
        return sig;
    }

    public List<List<String>> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "NostrEvent{" +
                "content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", id='" + id + '\'' +
                ", kind=" + kind +
                ", pubkey='" + pubkey + '\'' +
                ", sig='" + sig + '\'' +
                ", tags=" + tags +
                '}';
    }

    public static void main(String[] args) {
        String jsonString = "[\"EVENT\",\"subscription\",{\"content\":\"Summary of Secret Service needs 'fundamental reform,' panel examining Trump assassination attempt says ... \"}]";

        // Create a new Gson instance
        Gson gson = new Gson();

        // Parse the JSON string
        String eventJson = jsonString.split(",", 3)[2].replace("]", "");
        NostrEvent event = gson.fromJson(eventJson, NostrEvent.class);

        // Print out the parsed event
        System.out.println(event);
    }
}