/*
 * Encrypts text based on two secret words
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.utils.cybersec;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @author Brito
 * @date: 2024-09-24
 * @location: Germany
 */
public class DoubleCrypt {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    // Generate a single AES key from key1 and key2
    private static SecretKey generateKey(String key1, String key2) {
        try {
            String combinedKey = key1 + key2; // Combine the keys
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // Use SHA-256 to hash
            byte[] keyBytes = digest.digest(combinedKey.getBytes("UTF-8")); // Hash the combined keys
            return new SecretKeySpec(keyBytes, ALGORITHM); // Create SecretKey
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            return null; // Return null on failure
        }
    }

    // Encrypt the text using AES
    public static String encode(String key1, String key2, String text) {
        try {
            SecretKey key = generateKey(key1, key2);
            if (key == null) {
                return null; // Check if key generation failed
            }
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            byte[] iv = new byte[cipher.getBlockSize()];
            new SecureRandom().nextBytes(iv); // Generate a random IV
            IvParameterSpec ivParams = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
            byte[] encryptedBytes = cipher.doFinal(text.getBytes());

            // Compute hash of the original text
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes("UTF-8"));
            String hashString = Base64.getEncoder().encodeToString(hash);

            // Combine IV, encrypted text, and hash for storage/transmission
            byte[] combined = new byte[iv.length + encryptedBytes.length + hash.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);
            System.arraycopy(hash, 0, combined, iv.length + encryptedBytes.length, hash.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | UnsupportedEncodingException e) {
            return null; // Return null on failure
        }
    }

    // Decrypt the text using AES
    public static String decode(String key1, String key2, String encryptedText) {
        try {
            SecretKey key = generateKey(key1, key2);
            if (key == null) {
                return null; // Check if key generation failed
            }
            byte[] combined = Base64.getDecoder().decode(encryptedText);
            byte[] iv = new byte[16]; // AES block size is 16 bytes
            System.arraycopy(combined, 0, iv, 0, iv.length);

            byte[] encryptedBytes = new byte[combined.length - iv.length - 32]; // Exclude IV and hash
            System.arraycopy(combined, iv.length, encryptedBytes, 0, encryptedBytes.length);

            byte[] originalHash = new byte[32]; // SHA-256 produces a 32-byte hash
            System.arraycopy(combined, iv.length + encryptedBytes.length, originalHash, 0, originalHash.length);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            String decryptedText = new String(decryptedBytes);

            // Verify hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] decryptedHash = digest.digest(decryptedText.getBytes("UTF-8"));

            if (!Base64.getEncoder().encodeToString(decryptedHash).equals(Base64.getEncoder().encodeToString(originalHash))) {
                return null; // Integrity check failed
            }

            return decryptedText; // Return decrypted text if successful
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | UnsupportedEncodingException e) {
            return null; // Return null on failure
        }
    }

    // JUnit tests
    public static void main(String[] args) {
        testEncodeDecode();
        testNullKeyGeneration();
    }

    private static void testEncodeDecode() {
        String key1 = "firstKey";
        String key2 = "secondKey";
        String originalText = "Hello World";

        String encryptedText = DoubleCrypt.encode(key1, key2, originalText);
        String decryptedText = DoubleCrypt.decode(key1, key2, encryptedText);
        System.out.println("Encrypted content: " + encryptedText);
        assert originalText.equals(decryptedText) : "Test failed: Decrypted text does not match original.";
        System.out.println("Encode/Decode test passed!");
    }

    private static void testNullKeyGeneration() {
        String encryptedText = DoubleCrypt.encode(null, null, "Hello");
        assert encryptedText == null : "Test failed: Encryption should return null for null keys.";
        System.out.println("Null key test passed!");
    }
}
