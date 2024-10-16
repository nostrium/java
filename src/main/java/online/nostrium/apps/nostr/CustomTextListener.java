package online.nostrium.apps.nostr;

import nostr.base.Relay;
import nostr.connection.impl.listeners.TextListener;
import nostr.context.Context;
import okhttp3.WebSocket;

public class CustomTextListener extends TextListener {

    public CustomTextListener(Relay relay, Context context) {
        super(relay, context);
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        // Intercept the message here
        System.out.println("Intercepted message: " + message);
        
        // Modify the message or perform additional logic if necessary
        
        // Optionally, call the original logic
    }
}
