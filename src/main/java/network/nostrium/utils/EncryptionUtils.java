/*
 * Encrypt and decrypt text portions
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package network.nostrium.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author Brito
 * @date: 2024-08-05
 * @location: Germany
 */
public class EncryptionUtils {

    private static final String SALT = "1234578"; // Use a securely generated salt in a real application
    private static final int ITERATIONS = 65536;
    private static final int KEY_SIZE = 256;

    public static String encrypt(String plaintext, String password) {
        try {
            // Generate key from password
            SecretKeySpec key = createSecretKey(password);

            // Create cipher instance and initialize
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv = new byte[cipher.getBlockSize()];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);

            // Perform encryption
            byte[] encryptedText = cipher.doFinal(plaintext.getBytes("UTF-8"));

            // Encode to Base64 for easier storage and transfer
            return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(encryptedText);
        } catch (Exception e) {
            return null;
        }
    }

    public static String decrypt(String ciphertext, String password) {
        try {
            // Split the Base64 encoded string to extract IV and encrypted text
            String[] parts = ciphertext.split(":");
            byte[] iv = Base64.getDecoder().decode(parts[0]);
            byte[] encryptedText = Base64.getDecoder().decode(parts[1]);

            // Generate key from password
            SecretKeySpec key = createSecretKey(password);

            // Create cipher instance and initialize
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);

            // Perform decryption
            byte[] decryptedText = cipher.doFinal(encryptedText);

            return new String(decryptedText, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    private static SecretKeySpec createSecretKey(String password) {
        try {
            // Use PBKDF2 to derive a key from the password
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(), ITERATIONS, KEY_SIZE);
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        } catch (Exception e) {
            return null;
        }
    }

}
