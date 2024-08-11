package online.nostrium.utils.nostr;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NostrSign {

    // secp256k1 curve parameters
    private static final BigInteger P = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16);
    private static final BigInteger A = BigInteger.ZERO;
    private static final BigInteger B = new BigInteger("7", 16);
    private static final BigInteger N = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16);
    private static final BigInteger Gx = new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16);
    private static final BigInteger Gy = new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16);
    private static final ECPoint G = new ECPoint(Gx, Gy);

    // ECPoint class for representing points on the curve
    private static class ECPoint {
        BigInteger x;
        BigInteger y;

        ECPoint(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }

        ECPoint add(ECPoint other) {
            if (this.x.equals(other.x) && this.y.equals(other.y)) {
                return this.twice();
            }
            BigInteger slope = (other.y.subtract(this.y)).multiply(other.x.subtract(this.x).modInverse(P)).mod(P);
            BigInteger xr = slope.multiply(slope).subtract(this.x).subtract(other.x).mod(P);
            BigInteger yr = slope.multiply(this.x.subtract(xr)).subtract(this.y).mod(P);
            return new ECPoint(xr, yr);
        }

        ECPoint twice() {
            BigInteger slope = (this.x.multiply(this.x).multiply(new BigInteger("3")).add(A)).multiply(this.y.multiply(new BigInteger("2")).modInverse(P)).mod(P);
            BigInteger xr = slope.multiply(slope).subtract(this.x.multiply(new BigInteger("2"))).mod(P);
            BigInteger yr = slope.multiply(this.x.subtract(xr)).subtract(this.y).mod(P);
            return new ECPoint(xr, yr);
        }

        ECPoint multiply(BigInteger k) {
            ECPoint result = new ECPoint(BigInteger.ZERO, BigInteger.ZERO);
            ECPoint addend = this;

            for (int i = 0; i < k.bitLength(); i++) {
                if (k.testBit(i)) {
                    result = result.add(addend);
                }
                addend = addend.twice();
            }
            return result;
        }
    }

    /**
     * Generates a signature for a Nostr message using the provided private key.
     *
     * @param nsec The Bech32-encoded private key (nsec)
     * @param message The message to be signed
     * @return The generated signature as a hexadecimal string
     */
    public static String generateSignature(String nsec, String message) {
        try {
            // Step 1: Decode the Bech32 'nsec' to retrieve the private key bytes
            byte[] privateKeyBytes = decodeBech32ToBytes(nsec);
            BigInteger privateKey = new BigInteger(1, privateKeyBytes);

            // Step 2: Hash the message using SHA-256
            byte[] messageHash = sha256(message.getBytes("UTF-8"));

            // Step 3: Generate a random k
            SecureRandom random = new SecureRandom();
            BigInteger k;
            ECPoint R;
            do {
                k = new BigInteger(N.bitLength(), random).mod(N);
                R = G.multiply(k);
            } while (R.x.mod(N).equals(BigInteger.ZERO));

            // Step 4: Calculate r and s
            BigInteger r = R.x.mod(N);
            BigInteger s = k.modInverse(N).multiply(new BigInteger(1, messageHash).add(r.multiply(privateKey))).mod(N);

            // Ensure s is in the lower half of N
            if (s.compareTo(N.divide(BigInteger.valueOf(2))) > 0) {
                s = N.subtract(s);
            }

            // Step 5: Ensure r and s are 32 bytes long, and concatenate them
            byte[] signature = concatenate(padTo32Bytes(r.toByteArray()), padTo32Bytes(s.toByteArray()));

            // Step 6: Return the signature as a hexadecimal string
            return bytesToHex(signature);
        } catch (Exception e) {
            Logger.getLogger(NostrSign.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    private static byte[] decodeBech32ToBytes(String bech32) {
        // Strip the 'nsec1' prefix (or 'npub1')
        if (bech32.startsWith("nsec1")) {
            return bech32Decode(bech32.substring(5));
        } else {
            throw new IllegalArgumentException("Invalid Bech32 prefix");
        }
    }

    /**
     * Simple Bech32 decoding implementation.
     * Replace this with a full Bech32 implementation as needed.
     */
    private static byte[] bech32Decode(String bech32) {
        int[] charsetRev = new int[128];
        Arrays.fill(charsetRev, -1);
        final String CHARSET = "qpzry9x8gf2tvdw0s3jn54khce6mua7l";
        for (int i = 0; i < CHARSET.length(); i++) {
            charsetRev[CHARSET.charAt(i)] = i;
        }

        int dataLength = bech32.length() * 5 / 8;
        byte[] data = new byte[dataLength];
        int acc = 0;
        int bits = 0;
        int dataIndex = 0;

        for (int i = 0; i < bech32.length(); i++) {
            int value = charsetRev[bech32.charAt(i)];
            if (value == -1) throw new IllegalArgumentException("Invalid Bech32 character");

            acc = (acc << 5) | value;
            bits += 5;

            if (bits >= 8) {
                bits -= 8;
                data[dataIndex++] = (byte) ((acc >> bits) & 0xFF);
            }
        }

        return Arrays.copyOfRange(data, 0, dataIndex);
    }

    public static byte[] sha256(byte[] input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(input);
    }

    private static byte[] concatenate(byte[] r, byte[] s) {
        byte[] result = new byte[64];
        System.arraycopy(r, 0, result, 0, r.length);
        System.arraycopy(s, 0, result, 32, s.length);
        return result;
    }

    private static byte[] padTo32Bytes(byte[] bytes) {
        if (bytes.length == 32) {
            return bytes; // No padding needed
        } else if (bytes.length > 32) {
            // If the array is longer than 32 bytes, trim the leading byte(s)
            return Arrays.copyOfRange(bytes, bytes.length - 32, bytes.length);
        } else {
            // If the array is shorter than 32 bytes, pad with leading zeros
            byte[] padded = new byte[32];
            System.arraycopy(bytes, 0, padded, 32 - bytes.length, bytes.length);
            return padded;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Example usage
    public static void main(String[] args) {
        String nsec = "nsec1a94uxuxm7zhpmc4spdt7hq9ml9u920m46gec42edy7dyvuxn3l8s2kk4zh";
        String message = "Hello, Nostr!";
        String signature = generateSignature(nsec, message);
        if (signature != null) {
            System.out.println("Signature: " + signature);
        } else {
            System.out.println("Failed to generate signature.");
        }
    }
}
