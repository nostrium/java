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
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Date: 2023-02-09
 * @Place: Germany
 * @author brito
 */
public class TextFunctions {
    
    public static String getLastModifiedISO(File file) {
        // Get the last modified time in milliseconds
        long lastModified = file.lastModified();
        
        // Create a Date object
        Date date = new Date(lastModified);
        
        // Create a SimpleDateFormat to format the date in ISO 8601 format
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        
        // Return the formatted date as a string
        return isoFormat.format(date);
    }
    
     public static String humanReadableFileSize(File file) {
        long bytes = file.length();
        if (bytes < 1024) {
            return bytes + " B";
        } else {
            int exp = (int) (Math.log(bytes) / Math.log(1024));
            char sizePrefix = "KMGTPE".charAt(exp - 1); // Kilobyte, Megabyte, Gigabyte, etc.
            double size = bytes / Math.pow(1024, exp);
            return String.format("%.2f %sB", size, sizePrefix);
        }
    }

    public static boolean isValidNumberInRange(
            String numberStr, int lowerBound, int upperBound) {
        try {
            int number = Integer.parseInt(numberStr);

            // Check if the number falls within the specified range
            return number >= lowerBound && number <= upperBound;
        } catch (NumberFormatException e) {
            // If the string is not a valid integer, return false
            return false;
        }
    }

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

    public static String convertLongToDateTimeWithSeconds(long timestamp) {
        // Convert the long timestamp to an Instant assuming it's in milliseconds
        Instant instant = Instant.ofEpochMilli(timestamp);

        // Convert the Instant to a LocalDateTime in the system's default time zone
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // Define the date format including seconds
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    /**
     * Replaces the specified line in the ArrayList based on the content.
     *
     * This method searches for the first occurrence of the specified old line
     * in the ArrayList and replaces it with the new line. If the old line is
     * not found, the method returns the list unchanged.
     *
     * @param list the ArrayList of strings where the line replacement is to be
     * made
     * @param oldLine the line that should be replaced
     * @param newLine the new line that will replace the old line
     */
    public static void updateLineByContent(ArrayList<String> list, String oldLine, String newLine) {
        // Find the index of the old line
        int index = list.indexOf(oldLine);

        // If the line exists, replace it
        if (index != -1) {
            list.set(index, newLine);  // Replace the element at the found index
        }
        // If the line is not found, return without making any changes
    }

    public static String extractTextBetweenTicks(String inputText) {
        // Split the input text into lines
        String anchor = "\n```";

        int i1 = inputText.indexOf(anchor);
        if (i1 < 0) {
            return null;
        }
        String text = inputText.substring(i1 + anchor.length());
        int i2 = text.indexOf(anchor);
        text = text.substring(0, i2);

        // Return the extracted text
        return text;
    }

    public static String createLineWithText(int N, String text) {
        // Use StringBuilder to create a line with N spaces
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < N; i++) {
            spaces.append(text);
        }
        return spaces.toString();
    }

    public static String trimLeft(String input) {
        return input.replaceAll("^\\s+", "");  // ^\s+ matches leading whitespace
    }

    public static String trimRight(String input) {
        return input.replaceAll("\\s+$", "");  // \s+$ matches trailing whitespace
    }

    // Standalone static method to convert HashMap to String[] list
    public static String[] convertMapToStringArray(HashMap<String, String> map) {
        String[] result = new String[map.size()];
        int index = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            result[index++] = entry.getKey() + ": " + entry.getValue();
        }
        return result;
    }
    
    // Standalone static method to convert HashMap to String[] list
    @SuppressWarnings({"UnnecessaryTemporaryOnConversionFromString", "CollectionsToArray"})
    public static String[] convertMapToStringArrayOnlyNumbers(HashMap<String, Object> map) {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object object = entry.getValue();
            if(object instanceof Integer == false){
                continue;
            }
            int value = (int) entry.getValue();
            list.add(entry.getKey() + ": " + value);
        }
        return list.toArray(new String[0]);
    }
    
     // Function to calculate the number of spaces needed to center the number
    public static int calculateCenterSpaces(int number, int lineSize) {
        // Calculate available space on the line after subtracting the number's length
        int availableSpace = lineSize - number;
        
        // Return half of the available space to center the number
        return availableSpace / 2;
    }

    public static String generateVerb(String text) {
        text = text.toLowerCase();
        if(text.endsWith("s") == false){
            text += "s";
        }
        return text;
    }
    
}
