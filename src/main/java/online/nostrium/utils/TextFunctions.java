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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Date: 2023-02-09 Place: Germany
 *
 * @author brito
 */
public class TextFunctions {

    public static String convertLongToDateTime(long timestamp) {
        // Convert the long timestamp to an Instant
        Instant instant = Instant.ofEpochSecond(timestamp);

        // Convert the Instant to a LocalDateTime in the system's default time zone
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Format the LocalDateTime and return it as a string
        return dateTime.format(formatter);
    }

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
                //.append("|")
                .append("\n");

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

    public static void addIfNew(String text, ArrayList<String> list) {
        if (text == null || text.trim().length() == 0) {
            return;
        }
        if (list.contains(text)) {
            return;
        }
        // add it up
        list.add(text);
    }

//    public static String sanitizeChatMessage(String message) {
//        if (message == null || message.isEmpty()) {
//            return message;
//        }
//
//        // Normalize the text to decompose characters with diacritics into base characters
//        message = Normalizer.normalize(message, Normalizer.Form.NFKC);
//
//        // Regular expression to detect and remove invisible characters
//        Pattern invisibleCharacters = Pattern.compile("[\\p{C}\\p{Z}]");
//        message = invisibleCharacters.matcher(message).replaceAll("");
//
//        // Regular expression to allow common characters in conversations (letters, numbers, punctuation, and common symbols)
//        String allowedCharacters = "[^\\p{L}\\p{Nd}\\p{P}\\p{Zs}\\p{Zl}\\p{Zp}\\p{Sm}]";
//        message = message.replaceAll(allowedCharacters, "");
//
//        // Trim the result to remove leading or trailing whitespace
//        return message.trim();
//    }
    
   public static String sanitizeChatMessage(String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }

        StringBuilder sanitized = new StringBuilder(message.length());

        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);

            // Check for control characters and skip them, but allow standard whitespace characters
            if (Character.isISOControl(ch) && ch != '\t' && ch != '\n' && ch != '\r') {
                continue; // Skip control characters except tab, newline, carriage return
            }

            // Skip surrogate pairs and non-character code points
            if (Character.isSurrogate(ch) || !Character.isDefined(ch)) {
                continue;
            }

            // Append valid characters, including spaces, punctuation, and other visible characters
            sanitized.append(ch);
        }

        // Trim leading and trailing spaces and return the sanitized string
        return sanitized.toString().trim();
    }
   

    public static boolean isValidText(String message) {
        if (message == null || message.isEmpty()) {
            return false;
        }

        // Normalize the text to decompose characters with diacritics into base characters
        message = Normalizer.normalize(message, Normalizer.Form.NFKC);

        // Regular expression to detect and remove invisible characters
        Pattern invisibleCharacters = Pattern.compile("[\\p{C}\\p{Z}]");
        String sanitizedMessage = invisibleCharacters.matcher(message).replaceAll("");

        // Regular expression to allow common characters in conversations (letters, numbers, punctuation, and common symbols)
        String allowedCharacters = "[^\\p{L}\\p{Nd}\\p{P}\\p{Zs}\\p{Zl}\\p{Zp}\\p{Sm}]";
        sanitizedMessage = sanitizedMessage.replaceAll(allowedCharacters, "").trim();

        // Return true if the sanitized message is non-empty after all checks
        return !sanitizedMessage.isEmpty();
    }

}
