package online.nostrium.apps.nostr;

import lombok.extern.java.Log;
import nostr.api.NIP01;
import nostr.event.impl.Filters;
import nostr.event.Kind;
import nostr.id.Identity;
import nostr.util.NostrException;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import nostr.api.EventNostr;
import nostr.api.Nostr;
import nostr.event.impl.GenericEvent;
import online.nostrium.nostr.TextListenerCustom;

@Log
public class NostrGlobalPostsExample {

    private static final Identity SENDER = Identity.generateRandomIdentity();
    private final static Map<String, String> RELAYS = Map.of(
            "damus", "wss://relay.damus.io", 
            "wellorder", "wss://nostr-pub.wellorder.net", 
            "snort", "wss://relay.snort.social"
    );

    public static void main(String[] args) throws Exception {
        try {
            log.log(Level.FINE, "================= Start Fetching Global Posts");

            ExecutorService executor = Executors.newFixedThreadPool(10);

            // Submit task to fetch global posts
            executor.submit(() -> {
                try {
                    filters();
                } catch (InterruptedException t) {
                    log.log(Level.SEVERE, t.getMessage(), t);
                }
            });

            stop(executor);

            if (executor.isTerminated()) {
                log.log(Level.FINE, "================== The End");
            }

        } catch (IllegalArgumentException ex) {
            log.log(Level.SEVERE, null, ex);
            throw new NostrException(ex);
        }
    }

    /**
     * Fetches global posts (text notes) from relays using filters.
     */
    public static void filters() throws InterruptedException {
        //logHeader("filters");

        // Define the kinds of events you want to fetch (Kind 1: text notes)
        var kinds = List.of(Kind.TEXT_NOTE);

        // Time filter: Get posts from now
        var date = Calendar.getInstance();
        date.add(Calendar.DAY_OF_MONTH, -0);

        // Create filters
        Filters filters = Filters.builder()
                .kinds(kinds) // Kind 1 is text note events (global posts)
                .since(date.getTimeInMillis() / 1000) // Get posts from now
                .build();

        
        // Create the NIP01 instance and set the sender identity
        var nip01 = new NIP01<>(SENDER); // Explicitly set the sender identity here
        EventNostr eventNostr = nip01.setRelays(RELAYS);
        
        
        eventNostr.send(filters, "global-posts");
        
        
        
        // Set custom listener for intercepting messages
        List<GenericEvent> events = filters.getEvents();
        
        System.gc();
        
        
      
        // Wait for events (adjust timing as necessary)
        Thread.sleep(5000);
    }

  
    private static void stop(ExecutorService executor) {
        try {
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.log(Level.SEVERE, "termination interrupted");
        } finally {
            if (!executor.isTerminated()) {
                log.log(Level.SEVERE, "killing non-finished tasks");
            }
            executor.shutdownNow();
        }
    }
}
