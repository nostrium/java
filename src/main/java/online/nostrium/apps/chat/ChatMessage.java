package online.nostrium.apps.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.apps.user.User;
import online.nostrium.nostr.NostrSign;
import online.nostrium.nostr.NostrUtils;

public class ChatMessage {

    @Expose
    String id;

    @Expose
    String pubkey;  // This will be the raw hex format

    @Expose
    long created_at;  // Nostr compatible field name

    @Expose
    final int kind = 1;  // Always kind = 1 for chat messages

    @Expose
    final ArrayList<String[]> tags;

    @Expose
    String content;

    @Expose
    String sig;  // Signature field

    public ChatMessage(User user, String content, ArrayList<String[]> tags) {
        this.pubkey = NostrUtils.bech32ToHex(user.getNpub());  // Convert npub to hex format
        this.created_at = System.currentTimeMillis() / 1000L; // current timestamp in seconds
        this.tags = tags;
        this.content = content;
        this.id = generateId();
        this.sig = generateSignature(user);
    }

    public ChatMessage(User user, String content) {
        this.pubkey = NostrUtils.bech32ToHex(user.getNpub());  // Convert npub to hex format
        this.created_at = System.currentTimeMillis() / 1000L; // current timestamp in seconds
        this.tags = new ArrayList<>(); // Initialize tags to an empty list
        this.content = content;
        this.id = generateId();
        this.sig = generateSignature(user);
    }

    /**
     * Generates a unique ID for the message based on Nostr serialization.
     *
     * @return The unique ID as a hexadecimal string
     */
    private String generateId() {
        try {
            // Serialize the event as a JSON array according to Nostr's specification
            String serializedEvent = "[" + 0 + ",\"" + pubkey + "\"," + created_at + "," + kind + "," + serializeTags() + ",\"" + content + "\"]";

            // Compute SHA-256 hash
            byte[] hash = NostrSign.sha256(serializedEvent.getBytes("UTF-8"));

            // Convert to hexadecimal format
            return NostrUtils.bytesToHex(hash);
        } catch (Exception e) {
            Logger.getLogger(ChatMessage.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * Generates a signature for the message using the author's private key.
     *
     * @param user The User object containing the private key
     * @return The generated signature as a hexadecimal string
     */
    private String generateSignature(User user) {
        try {
            // Use the NostrSign class to generate the signature
            return NostrSign.generateSignature(user.getNsec(), id);
        } catch (Exception e) {
            Logger.getLogger(ChatMessage.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * Serialize the tags as a JSON array.
     *
     * @return Serialized tags as a JSON array string
     */
    private String serializeTags() {
        Gson gson = new Gson();
        return gson.toJson(tags);
    }

    /**
     * Export this object as a JSON string.
     *
     * @return JSON string or null if something went wrong
     */
    public String jsonExport() {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .setPrettyPrinting();
            Gson gson = gsonBuilder.create();
            return gson.toJson(this);
        } catch (Exception e) {
            Logger.getLogger(ChatMessage.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
}
