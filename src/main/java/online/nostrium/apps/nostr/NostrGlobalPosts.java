/*
 * Get the global posts from a specific relay
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.nostr;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import java.time.Instant;

/**
 * @author Brito
 * @date: 2024-10-17
 * @location: Portugal
 */
public class NostrGlobalPosts {

    // Static method to query Nostr relay
    public static void queryNostrRelay(String relayUrl, long sinceTimestamp) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(relayUrl).build();
        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                // Send subscription request with the since filter
                String subscriptionRequest = "[\"REQ\", \"subscription\", {"
                        + "\"kinds\": [1],"            // Kind 1 for text messages
                        + "\"since\": " + sinceTimestamp
                        + "}]";
                webSocket.send(subscriptionRequest);
                System.out.println("Subscription sent: " + subscriptionRequest);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                // Handle incoming message
                System.out.println("Received message: " + text);
                try{
                    
                    
                    
                }catch (Exception E){
                    System.out.println("Failed to parse: " + text);
                }
                
                
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                // Handle incoming binary message
                System.out.println("Received binary message: " + bytes.hex());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, null);
                System.out.println("Closing WebSocket: " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                t.printStackTrace();
            }
        };

        // Create WebSocket connection
        WebSocket webSocket = client.newWebSocket(request, listener);

        // Trigger shutdown of the dispatcher's executor to clean up resources.
        client.dispatcher().executorService().shutdown();
    }

    // Utility to convert a date string to a Unix timestamp
    public static long getUnixTimestamp(String date) {
        Instant instant = Instant.parse(date + "T00:00:00Z");
        return instant.getEpochSecond();
    }

    public static void main(String[] args) {
        // Example usage
        String relayUrl = "wss://articles.layer3.news";  // Replace with actual Nostr relay URL
        long sinceTimestamp = getUnixTimestamp("2024-10-17"); // Replace with desired date
        queryNostrRelay(relayUrl, sinceTimestamp);
    }

    
}
