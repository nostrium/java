/*
 * Internal utils
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.utils;

import nostr.id.Identity;

/**
 * Utility class for generating Nostr keys and signing messages.
 * 
 * Author: Brito
 * Date: 2024-08-06
 * Location: Germany
 */
public class NostrUtils {


    /**
     * Generates a Nostr key pair, returning the nsec (private key) and npub (public key).
     *
     * @return A string array with the nsec (private key) and npub (public key) in hexadecimal format
     */
    public static String[] generateNostrKeys() {
        Identity user = Identity.generateRandomIdentity();
        String nsec = user.getPrivateKey().toBech32String();
        String npub = user.getPublicKey().toBech32String();
        return new String[]{nsec, npub};
    }

    
}
