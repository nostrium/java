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
import online.nostrium.utils.events.Action;
import online.nostrium.utils.events.ActionResult;
import online.nostrium.utils.events.ActionType;
import online.nostrium.utils.events.EventIndex;

/**
 * @author Brito
 * @date: 2024-08-31
 * @location: Germany
 */
public class NIP05_emails {

    public NIP05_emails() {
        //launchUpdateDataThreaded();
        Action action = new ActionNIP05(this);
        core.events.addAction(EventIndex.register, action);
    }

    public void updateData() {
        NIP05 nip = new NIP05();
        nip.scan();
        nip.save();
    }

//    private void launchUpdateDataThreaded() {
//        // Create a new thread to run the updateData() method
//        Thread updateThread = new Thread(() -> {
//            while (true) {
//                try {
//                    updateData();
//                    if(core.config.debug){
//                        Log.write("NIP05-scheduled", TerminalCode.OK, "Generate nostr.json", null);
//                    }
//                    // Sleep for a specified amount of time before running the update again
//                    Thread.sleep(600000); // 600 seconds (10 minutes)
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    Thread.currentThread().interrupt();
//                }
//            }
//        });
//
//        // Start the thread
//        updateThread.start();
//    }
}

class ActionNIP05 extends Action{
    
    final NIP05_emails nip05;
    
    ActionNIP05(NIP05_emails nip05){
        this.nip05 = nip05;
    }

    @Override
    protected String getId() {
        return "Action-NIP05";
    }

    @Override
    protected ActionResult doAction(Object object) {
        return new ActionResult(ActionType.NOTHING, "", null);
    }

    @Override
    protected ActionResult before(Object object) {
        return new ActionResult(ActionType.NOTHING, "", null);
    }

    @Override
    protected ActionResult after(Object object) {
        nip05.updateData();
                    if(core.config.debug){
                        Log.write("NIP05-scheduled", TerminalCode.OK, "Generate nostr.json", null);
                    }
        return new ActionResult(ActionType.OK, "Created new nostr.json", null);
    }
}
