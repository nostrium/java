/*
 * Standalone signature of events
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.utils;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;


/**
 * Author: Brito 
 * Date: 2024-08-10 Location: Germany
 */
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
     * @return The generated signature as a Base64-encoded string
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

            // Step 5: Encode r and s as a concatenated byte array
            byte[] signature = concatenate(r.toByteArray(), s.toByteArray());

            // Step 6: Return the signature as a Base64-encoded string
            return Base64.getEncoder().encodeToString(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] decodeBech32ToBytes(String bech32) {
        // Implement Bech32 decoding here
        // Placeholder: Simplified decoding assuming Base64
        String base32Part = bech32.substring(5);
        return Base64.getDecoder().decode(base32Part);
    }

    public static byte[] sha256(byte[] input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(input);
    }

    private static byte[] concatenate(byte[] r, byte[] s) {
        byte[] result = new byte[r.length + s.length];
        System.arraycopy(r, 0, result, 0, r.length);
        System.arraycopy(s, 0, result, r.length, s.length);
        return result;
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
