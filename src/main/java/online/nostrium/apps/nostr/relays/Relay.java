/*
 * Defines a NOSTR relay
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.nostr.relays;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import online.nostrium.utils.time;

/**
 * @author Brito
 * @date: 2024-10-17
 * @location: Portugal
 */
public class Relay {

    final String relayURL;
    String countryCode = "NONE";
    long eventsPerHour = 0;
    protected boolean isRunning = false, paused = false;
    long sinceTimestamp = time.getCurrentTimeInUnix() - 10 * 1000;

    boolean debug = false;

    String anchor = "[\"EVENT\",\"subscription\",";
    ArrayList<NostrEvent> messages = new ArrayList<>();

    public Relay(String URL) {
        if (!URL.startsWith("ws")) {
            URL = "wss://" + URL;
        }
        this.relayURL = URL;
    }

    public void start() {
        @SuppressWarnings("Convert2Lambda")
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                isRunning = true;
                while (isRunning) {
                    try {
                        getNewTexts();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Error occurred: " + e.getMessage());
                    }
                    time.wait(10);
                    // is pause enabled?
                    while (isPaused()) {
                        time.wait(2);
                    }
                }
            }
        });
        thread.start();
        time.wait(1);
    }

    public boolean isRunning() {
        return isRunning;
    }

    private void getNewTexts() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)  // Disable read timeout
                .build();

        Request request = new Request.Builder().url(relayURL).build();
        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                String subscriptionRequest = "[\"REQ\", \"subscription\", {"
                        + "\"kinds\": [1]," // Kind 1 for text messages
                        + "\"since\": " + sinceTimestamp
                        + "}]";
                webSocket.send(subscriptionRequest);
                if (debug) {
                    System.out.println("Subscription sent: " + subscriptionRequest);
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                if (!text.startsWith(anchor)) {
                    return;
                }
                text = text.substring(anchor.length(), text.length() - 1);
                Gson gson = new Gson();
                try {
                    NostrEvent event = gson.fromJson(text, NostrEvent.class);
                    if(event == null){
                        return;
                    }
                    if (event.getContent().isBlank()) {
                        return;
                    }
                    for (NostrEvent message : messages) {
                        if (message.getContent().equals(event.getContent())) {
                            return;
                        }
                    }
                    messages.add(event);
                    System.out.println(event.getContent());
                    System.out.println();
                    System.out.println("https://nostr.band/?q=" + event.getId());
                    System.out.println("---------");
                } catch (JsonSyntaxException e) {
                    //e.printStackTrace();
                    System.out.println("Error processing message: " + e.getMessage());
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                // Handle binary message
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, null);
                if (debug) {
                    System.out.println("WebSocket closing: " + reason);
                    System.out.println("------------------------------------------------");
                }
//                time.wait(10);  // Delay before reconnecting
//                getNewTexts();  // Retry connection
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                if(debug){
                    t.printStackTrace();
                    System.out.println("WebSocket failure: " + t.getMessage());
                }
//                time.wait(10);  // Retry connection after failure
//                getNewTexts();
            }
        };

        WebSocket webSocket = client.newWebSocket(request, listener);
        time.wait(5);
        webSocket.close(1000, "Finished receiving updates");
        if (!messages.isEmpty()) {
            updateTimer();
        }
        client.dispatcher().executorService().shutdown();
    }

    private void updateTimer() {
        for (NostrEvent message : messages) {
            if (message.getCreatedAt() > sinceTimestamp) {
                sinceTimestamp = message.getCreatedAt();
            }
        }
        int max = 100;
        while (messages.size() > max) {
            messages.remove(0);
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    
    
    public static void main(String[] args) {
        //String relay01 = "wss://202.61.207.49:8090";
        //String relay01 = "wss://offchain.pub";
        String relay01 = "wss://articles.layer3.news";
        
        Relay relay = new Relay(relay01);
        relay.start();
        while (relay.isRunning()) {
            time.wait(10);
        }
    }

    
}
