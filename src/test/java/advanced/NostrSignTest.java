/*
 *  Convert an online forum to nostrium format
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package advanced;

import nostr.base.PrivateKey;
import nostr.crypto.bech32.Bech32;
import nostr.crypto.bech32.Bech32.Bech32Data;
import nostr.id.Identity;
import nostr.util.NostrException;
import online.nostrium.nostr.NostrUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

        assertEquals(63, RECIPIENT.getPrivateKey().toBech32String().length());
        assertEquals(63, RECIPIENT.getPublicKey().toBech32String().length());
    }


    @Test
    public void rebuildNsec() throws Exception {
        String nsec = "nsec1fat58gjcdwgjxlj97jlcf48smwktncqre280yesxawdjvx2xx8sswajwyn";
        String npub = "npub16q5kemyjcdnh7xt7es9624c6q6d3nlruz65v8ravvkxgcedg4susvf95se";

        // Create the PrivateKey object
        PrivateKey privateKey = NostrUtils.fromNsec(nsec);

        // Generate the NSEC string from the PrivateKey object
        String nsec2 = privateKey.toBech32String();

        // Assert that the original NSEC and the generated NSEC match
        assertEquals(nsec, nsec2);
        
        Identity id = Identity.create(privateKey);
        assertEquals(nsec, id.getPrivateKey().toBech32String());
        
    }

    
}
