/*
 * List of relays to be used
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.nostr.relays;

import java.util.ArrayList;

/**
 * @author Brito
 * @date: 2024-10-17
 * @location: Portugal
 */
public class RelayList {

    String[] list = new String[]{
        "wss://articles.layer3.news",
        "wss://offchain.pub",
        "ws://202.61.207.49:8090",
        "wss://nostr.me/relay"
    };
    
    ArrayList<Relay> relays = new ArrayList<>();
    
}
