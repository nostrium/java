/*
 * Utils for story telling
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.utils.MathFunctions;

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
    
    
//            line = line.trim();
//            
//            // does it start with our identifier
//            if(line.startsWith(startWithId)){
//                // was it recording before?
//                if(recording){
//                    // add the previous block being recorded
//                    blocks.add(currentText.toString());
//                    // start a new one
//                    currentText.setLength(0);
//                }else{
//                    // start recording right now
//                    recording = true;
//                }
//            }
//            
//            // are we recording and found a new block?
//            if(recording 
//                    && line.startsWith("# ")
//                    && line.startsWith(startWithId) == false
//                    ){
//                recording = false;
//                 // add the previous block being recorded
//                blocks.add(currentText.toString());
//                currentText.setLength(0);
//            }
//            
//            
//            // record the line
//            if(recording){
//                currentText.append(line).append("\n");
//            }
            
            
            
            
//            if(recording 
//                    && line.startsWith("# ") 
//                    && line.startsWith(startWithId) == false
//                    //&& line.startsWith("----")
//                    ){
//                recording = false;
//                blocks.add(currentText.toString().trim());
//                currentText.setLength(0);  // Reset for the next scene
//                continue;
//            }
    
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

    public static ArrayList<String> getTextBlocks(String startWithId, String source) {
        ArrayList<String> blocks = new ArrayList<>();
        String[] lines = source.split("\n");  // Split script into lines
        StringBuilder currentText = new StringBuilder();
        boolean recording = false;

        for (String line : lines) {
            
            if(recording && line.startsWith(startWithId) == false
                    && line.startsWith("# ")
                    ){
                blocks.add(currentText.toString().trim());
                currentText.setLength(0);  // Reset for the next scene
                recording = false;
                continue;
            }
                    
            if(recording && line.startsWith("-----")){
                blocks.add(currentText.toString().trim());
                currentText.setLength(0);  // Reset for the next scene
                recording = false;
                continue;
            }
            
            
            if (line.startsWith(startWithId)) {
                // If already recording a scene, save the previous scene
                if (recording && currentText.length() > 0) {
                    blocks.add(currentText.toString().trim());
                    currentText.setLength(0);  // Reset for the next scene
                }
                recording = true;  // Start recording the new scene
            }
            
            // Continue adding lines under the current scene
            if (recording) {
                currentText.append(line).append("\n");
            }
        }

        // Add the last scene if the loop ends while recording
        if (recording && currentText.length() > 0) {
            blocks.add(currentText.toString().trim());
        }

        return blocks;
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

    /**
     * Convert a Scene in markdown format to an HTML anchor
     * @param sceneName
     * @return 
     */
    static String convertSceneTitleToId(String sceneName) {
        String anchor = "# Scene: ";
        String title = sceneName.substring(anchor.length()).replace("\n", "");
        return "scene-" + title.toLowerCase().replace(" ", "-");
    }

    
    // Single method that normalizes the chances and selects a choice without throwing exceptions
    public static Choice selectRandomChoice(ArrayList<Choice> choices) {
        if (choices == null || choices.isEmpty()) {
            // Return null if the input list is null or empty
            return null;
        }

        // Step 1: Calculate the total sum of the chances
        int totalChance = 0;
        for (Choice choice : choices) {
            totalChance += choice.getChance();
        }

        // Step 2: Normalize the percentages if the total doesn't equal 100
        if (totalChance != 100) {
            for (Choice choice : choices) {
                // Normalization step: Scale each chance proportionally to make the sum 100%
                double normalizedChance = (double) choice.getChance() / totalChance * 100;
                choice.setChance((int) Math.round(normalizedChance));  // Update the chance value
            }
        }

        // Step 3: Select a random choice based on the normalized chances
        Random random = new Random();
        int randomNumber = random.nextInt(100) + 1; // Generate random number between 1 and 100

        int cumulativeChance = 0;
        for (Choice choice : choices) {
            cumulativeChance += choice.getChance();  // Sum the chances as we go
            if (randomNumber <= cumulativeChance) {
                return choice;  // Return the choice that falls within the cumulative range
            }
        }

        // If no valid choice is selected (shouldn't happen), return null
        return null;
    }
    
    
    /**
     * Compute the expression between two things
     *
     * @param expression
     * @param A
     * @param B
     * @return
     */
    public static String translate(String expression, GameThing A, GameThing B) {
        // A:Attack + (A:Experience / (A:Attack * 0,5))
        String result = expression;

        // change all A-related expressions
        for (String attr : A.getAttributes().keySet()) {
            String tag = "A:" + attr;
            if (result.contains(tag) == false) {
                continue;
            }
            String valueText = A.getAttributes().get(attr);
            result = result.replace(tag, valueText);
        }

        // change all B-related expressions
        for (String attr : B.getAttributes().keySet()) {
            String tag = "B:" + attr;
            if (result.contains(tag) == false) {
                continue;
            }
            String value = B.getAttributes().get(attr);
            result = result.replace(tag, value);
        }


        // now calculate the formula
        boolean convertedOK = false;
        long value = 0;
        try {
            value = MathFunctions.evaluateExpression(result);
            convertedOK = true;
        } catch (Exception ex) {
            //Logger.getLogger(GameThing.class.getName()).log(Level.SEVERE, null, ex);
        }

        // don't change the value when the output failed
        if (convertedOK) {
            result = value + "";
        }
        return result;
    }

}
