/*
 *  Convert an online forum to nostrium format
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package advanced;

import nostr.id.Identity;
import org.junit.jupiter.api.Test;

/**
 * @author Brito
 * @date: 2024-08-26
 * @location: Germany
 */
public class NostrSignTest {
    
    public NostrSignTest() {
    }

    @Test
    public void helloNostr() {
        
        Identity RECIPIENT = Identity.generateRandomIdentity();
        
        System.out.println("NSEC: " + RECIPIENT.getPrivateKey().toBech32String());
        System.out.println("NPUB: " + RECIPIENT.getPublicKey().toBech32String());
    
        System.gc();
    }
}
