/*
 * Utils for story telling
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.util.ArrayList;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class StoryUtils {

    public static void writeln(String text, String... vars) {
        String formattedText = String.format(text, (Object[]) vars);
        String[] parts = formattedText.split("\\.", 2); // Split at the first period
        if (parts.length >= 2) {
            System.out.println(parts[0].trim() + ".");
            System.out.println(parts[1].trim());
        } else {
            System.out.println(formattedText);
        }
    }

    public static String getTextBlock(String id, String source) {
        String[] lines = source.split("\n");  // Split script into lines
        StringBuilder output = new StringBuilder();
        boolean recording = false;

        for (String line : lines) {
            if (line.startsWith(id)) {
                recording = true;  // Start recording the new scene
            }

            // Continue adding lines under the current scene until another block or end of text
            if (recording) {
                if (line.startsWith("#") && !line.startsWith(id)) {
                    break;  // Stop recording when another block is encountered
                }
                output.append(line).append("\n");
            }
        }
        return output.toString().trim();  // Return the current scene as a single string
    }
    
    /**
     * Try to make sure the formatting is normalized
     * @param text
     * @return 
     */
    public static String normalize(String text) {
        // cases of text files created on windows
       text = text.replace("\r\n", "\n");
       return text;
    }

    public static ArrayList<String> getTextBlocks(String id, String source) {
        ArrayList<String> sceneTexts = new ArrayList<>();
        String[] lines = source.split("\n");  // Split script into lines
        StringBuilder currentScene = new StringBuilder();
        boolean recording = false;

        for (String line : lines) {
            if (line.startsWith(id)) {
                // If already recording a scene, save the previous scene
                if (recording && currentScene.length() > 0) {
                    sceneTexts.add(currentScene.toString().trim());
                    currentScene.setLength(0);  // Reset for the next scene
                }
                recording = true;  // Start recording the new scene
            }
            
//            if(line.startsWith("#") && line.startsWith(id) == false){
//                recording = false;
//            }
            

            // Continue adding lines under the current scene
            if (recording) {
                currentScene.append(line).append("\n");
            }
        }

        // Add the last scene if the loop ends while recording
        if (recording && currentScene.length() > 0) {
            sceneTexts.add(currentScene.toString().trim());
        }

        return sceneTexts;
    }
    
    public static ArrayList<String> getMatchingLines(String key, String script) {
        ArrayList<String> matchingLines = new ArrayList<>();
        String[] lines = script.split("\n");  // Split the script into lines

        for (String line : lines) {
            // Trim leading spaces and check if the line starts with the given key
            String trimmedLine = line.trim();
            if (trimmedLine.startsWith(key)) {
                matchingLines.add(trimmedLine);  // Add the line to the result if it matches the key
            }
        }
        return matchingLines;
    }


}
