package online.nostrium.utils.nostr;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

public class NostrUtils {

    private static final String BECH32_CHARSET = "qpzry9x8gf2tvdw0s3jn54khce6mua7l";
    private static final int[] BECH32_CHARSET_REV = new int[128];

    static {
        Arrays.fill(BECH32_CHARSET_REV, -1);
        for (int i = 0; i < BECH32_CHARSET.length(); i++) {
            BECH32_CHARSET_REV[BECH32_CHARSET.charAt(i)] = i;
        }
    }

    // secp256k1 curve parameters
    private static final BigInteger P = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16);
    private static final BigInteger A = BigInteger.ZERO;
    private static final BigInteger B = new BigInteger("7", 16);
    private static final BigInteger N = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16);
    private static final BigInteger Gx = new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16);
    private static final BigInteger Gy = new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16);
    private static final ECPoint G = new ECPoint(Gx, Gy);

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

    public static String[] generateNostrKeys() {
        SecureRandom random = new SecureRandom();
        byte[] privateKey = new byte[32];
        random.nextBytes(privateKey);

        // Convert the private key to Bech32 (nsec)
        String nsec = encodeBech32("nsec", privateKey);

        // Generate the public key (G * privateKey) and convert to Bech32 (npub)
        byte[] publicKey = derivePublicKey(privateKey);
        String npub = encodeBech32("npub", publicKey);

        return new String[]{nsec, npub};
    }

    private static String encodeBech32(String prefix, byte[] data) {
        StringBuilder bech32 = new StringBuilder(prefix + "1");

        int currentValue = 0;
        int bits = 0;
        for (byte b : data) {
            currentValue = (currentValue << 8) | (b & 0xFF);
            bits += 8;

            while (bits >= 5) {
                bits -= 5;
                int index = (currentValue >> bits) & 0x1F;
                bech32.append(BECH32_CHARSET.charAt(index));
            }
        }

        if (bits > 0) {
            int index = (currentValue << (5 - bits)) & 0x1F;
            bech32.append(BECH32_CHARSET.charAt(index));
        }

        return bech32.toString();
    }

    public static String bech32ToHex(String bech32Key) {
        try {
            String noPrefix = bech32Key.substring(bech32Key.indexOf("1") + 1);
            byte[] decoded = decodeBech32(noPrefix);
            StringBuilder hexString = new StringBuilder();
            for (byte b : decoded) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Bech32 string: " + bech32Key, e);
        }
    }

    private static byte[] decodeBech32(String bech32) {
        int[] values = new int[bech32.length()];
        for (int i = 0; i < bech32.length(); i++) {
            char c = bech32.charAt(i);
            if (c >= BECH32_CHARSET_REV.length || BECH32_CHARSET_REV[c] == -1) {
                throw new IllegalArgumentException("Invalid character in Bech32 string: " + c);
            }
            values[i] = BECH32_CHARSET_REV[c];
        }

        int bits = 0;
        int buffer = 0;
        int outputIndex = 0;
        byte[] output = new byte[(values.length * 5 + 7) / 8];
        for (int value : values) {
            buffer = (buffer << 5) | value;
            bits += 5;
            if (bits >= 8) {
                bits -= 8;
                output[outputIndex++] = (byte) ((buffer >> bits) & 0xFF);
            }
        }
        return Arrays.copyOf(output, outputIndex);  // Trim the array to the correct size
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static byte[] derivePublicKey(byte[] privateKey) {
        BigInteger privateKeyInt = new BigInteger(1, privateKey);
        ECPoint publicKeyPoint = G.multiply(privateKeyInt);

        byte[] publicKeyBytesX = padTo32Bytes(publicKeyPoint.x.toByteArray());
        byte[] publicKeyBytesY = padTo32Bytes(publicKeyPoint.y.toByteArray());

        byte[] publicKeyBytes = new byte[publicKeyBytesX.length + publicKeyBytesY.length];
        System.arraycopy(publicKeyBytesX, 0, publicKeyBytes, 0, publicKeyBytesX.length);
        System.arraycopy(publicKeyBytesY, 0, publicKeyBytes, publicKeyBytesX.length, publicKeyBytesY.length);

        return publicKeyBytes;
    }

    private static byte[] padTo32Bytes(byte[] byteArray) {
        if (byteArray.length == 32) {
            return byteArray;
        }

        byte[] paddedArray = new byte[32];
        if (byteArray.length < 32) {
            System.arraycopy(byteArray, 0, paddedArray, 32 - byteArray.length, byteArray.length);
        } else {
            // Handle the case where the array is longer than 32 bytes
            System.arraycopy(byteArray, byteArray.length - 32, paddedArray, 0, 32);
        }
        return paddedArray;
    }
}
