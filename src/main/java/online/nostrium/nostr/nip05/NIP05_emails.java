/*
 * NIP-05: Mapping Nostr keys to DNS-based internet identifiers
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.nostr.nip05;

import online.nostrium.logs.Log;
import online.nostrium.main.core;
import online.nostrium.servers.terminal.TerminalCode;

/**
 * @author Brito
 * @date: 2024-08-31
 * @location: Germany
 */
public class NIP05_emails {

    public NIP05_emails() {
        launchUpdateDataThreaded();
    }

    public void updateData() {
        NIP05 nip = new NIP05();
        nip.scan();
        nip.save();
    }

    private void launchUpdateDataThreaded() {
        // Create a new thread to run the updateData() method
        Thread updateThread = new Thread(() -> {
            while (true) {
                try {
                    updateData();
                    if(core.config.debug){
                        Log.write("NIP05-scheduled", TerminalCode.OK, "Generate nostr.json", null);
                    }
                    // Sleep for a specified amount of time before running the update again
                    Thread.sleep(600000); // 600 seconds (10 minutes)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Start the thread
        updateThread.start();
    }
}
