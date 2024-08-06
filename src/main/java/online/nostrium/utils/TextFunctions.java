/*
 *  Functions related to text manipulation
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.utils;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.profile.pegdown.Extensions;
import com.vladsch.flexmark.profile.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.util.data.DataHolder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Date: 2023-02-09
 * Place: Germany
 * @author brito
 */
public class TextFunctions {

    /**
     * Cleans a string from non-alphanumeric letters Finds equivalents for
     * letters with accents.
     *
     * @param input
     * @return
     */
    public static String cleanString(String input) {
        String text = Normalizer
                .normalize(input, Normalizer.Form.NFD)
                //.replaceAll("[^\\p{ASCII}]", "");
                .replaceAll("[^A-Za-z0-9\\s]", "");
        text = text.replaceAll("\\s", "_");
        return text.replaceAll("__", "_");
    }

    public static String convertHtmlToMarkdown(String input) {
        DataHolder options = PegdownOptionsAdapter.flexmarkOptions(true,
                Extensions.ALL);
        return FlexmarkHtmlConverter.builder(options).build().convert(input);
    }

    /**
     * Get the current year, month and day in ISO format
     *
     * @return e.g. 2024-08-03_22:01
     */
    public static String getDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
        return now.format(formatter);
    }
    
   /**
     * Returns an ASCII art window frame around the given title.
     *
     * @param title The title to be framed.
     * @return The framed title as a string.
     */
    public static String getWindowFrame(String title) {
        int paddingHorizontal = 4;
        int titleLength = title.length();
        int totalWidth = titleLength + paddingHorizontal * 2; // No extra spaces for the borders

        String topBottomBorder = "+" + new String(new char[totalWidth]).replace('\0', '-') + "+";

        StringBuilder sb = new StringBuilder();
        sb.append(topBottomBorder).append("\n");

        sb.append("|")
          .append(new String(new char[paddingHorizontal / 2]).replace('\0', ' '))
          .append(title)
          .append(new String(new char[paddingHorizontal / 2]).replace('\0', ' '))
          .append("|").append("\n");

        sb.append(topBottomBorder);

        return sb.toString();
    }

    
    public static String sha256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(text.getBytes());
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (int i = 0; i < encodedhash.length; i++) {
                String hex = Integer.toHexString(0xff & encodedhash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String sanitizePassword(String password) {
        if (password == null) {
            return null;
        }

        // Trim leading and trailing spaces
        password = password.trim();

        // Remove non-printable ASCII characters
        StringBuilder sanitized = new StringBuilder();
        for (char c : password.toCharArray()) {
            if (c >= 32 && c <= 126) { // Printable ASCII range
                sanitized.append(c);
            }
        }

        return sanitized.toString();
    }
    
}
