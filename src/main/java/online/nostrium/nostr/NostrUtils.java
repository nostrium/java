package online.nostrium.nostr;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nostr.api.NIP01;
import nostr.base.PrivateKey;
import nostr.crypto.bech32.Bech32;
import nostr.crypto.bech32.Bech32.Bech32Data;
import nostr.crypto.schnorr.Schnorr;
import nostr.event.BaseTag;
import nostr.event.impl.TextNoteEvent;
import nostr.event.tag.PubKeyTag;
import nostr.id.Identity;
import nostr.util.NostrException;

public class NostrUtils {

    public static String[] generateNostrKeys() {
        Identity user = Identity.generateRandomIdentity();
        String nsec = user.getPrivateKey().toBech32String();
        String npub = user.getPublicKey().toBech32String();
        return new String[]{nsec, npub};
    }

    public static Identity generateIdentity() {
        return Identity.generateRandomIdentity();
    }

    public static Identity generateFromNsec(String nsec) {
        PrivateKey privateKey = fromNsec(nsec);
        return Identity.create(privateKey);
    }
    
        
    public static PrivateKey fromNsec(String nsec) {
        try {
            // Decode the Bech32 encoded NSEC string
            Bech32Data decodedData = Bech32.decode(nsec);
            
            // Ensure the HRP is 'nsec'
            if (!"nsec".equals(decodedData.hrp)) {
                throw new NostrException("Invalid HRP in NSEC");
            }
            
            // Convert the 5-bit groups back to 8-bit groups to get the private key bytes
            byte[] privateKeyBytes = convertBits(decodedData.data, 5, 8, false);
            
            // Ensure the private key is 32 bytes long
            if (privateKeyBytes.length != 32) {
                return null;
            }
            
            return new PrivateKey(privateKeyBytes);
        } catch (NostrException ex) {
            return null;
        }
    }

    // Copied convertBits method from the Bech32 class
    private static byte[] convertBits(byte[] data, int fromWidth, int toWidth, boolean pad) throws NostrException {
        int acc = 0;
        int bits = 0;
        List<Byte> result = new ArrayList<>();
        int maxv = (1 << toWidth) - 1;
        for (byte value : data) {
            int b = value & 0xff;
            if ((b >> fromWidth) != 0) {
                throw new NostrException("Input data too large");
            }
            acc = (acc << fromWidth) | b;
            bits += fromWidth;
            while (bits >= toWidth) {
                bits -= toWidth;
                result.add((byte) ((acc >> bits) & maxv));
            }
        }
        if (pad) {
            if (bits > 0) {
                result.add((byte) ((acc << (toWidth - bits)) & maxv));
            }
        } else if (bits >= fromWidth || ((acc << (toWidth - bits)) & maxv) != 0) {
            throw new NostrException("Could not convert bits, invalid padding");
        }
        byte[] output = new byte[result.size()];
        for (int i = 0; i < output.length; i++) {
            output[i] = result.get(i);
        }
        return output;
    }
    
    

    public static Map<String, String> getRelaysDefault() {
        Map<String, String> relays = new LinkedHashMap<>();
        relays.put("lol", "nos.lol");
        relays.put("damus", "relay.damus.io");
        relays.put("primal", "relay.primal.net");
        relays.put("ZBD", "nostr.zebedee.cloud");
        relays.put("taxi", "relay.taxi");
        relays.put("mom", "nostr.mom");
        relays.put("eden", "eden.nostr.land");
        relays.put("plebstr", "relay.plebstr.com");
        relays.put("snort", "relay.snort.social");
        relays.put("jb55", "jb55.com");
        relays.put("fiatjaf", "nostr.fiatjaf.com");
        relays.put("able", "nostr.able.ms");
        relays.put("bitcoiner", "relay.bitcoiner.social");
        return relays;
    }

    public static  TextNoteEvent sendTextNoteEvent(
            String nsec,
            List<String> tagsToAdd,
            String message,
            Map<String, String> relays) {

        Identity user = generateFromNsec(nsec);
        //List<BaseTag> tagsToAdd = new ArrayList<>();
        PubKeyTag tagx = new PubKeyTag(user.getPublicKey());
        List<BaseTag> tags = new ArrayList<>(List.of(tagx));

        NIP01<TextNoteEvent> nip01 = new NIP01<>(user);
        nip01.createTextNoteEvent(tags, message);
        nip01.sign();
        nip01.send(relays);

        return nip01.getEvent();
    }

}
