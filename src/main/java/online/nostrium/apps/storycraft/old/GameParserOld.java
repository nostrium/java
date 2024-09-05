/*
 * Parses the game script and loads the scenes and items for the adventure, either from a file or a string.
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft.old;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import online.nostrium.apps.storycraft.Item;
import online.nostrium.apps.storycraft.Scene;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class GameParserOld {

    private final Map<String, Scene> scenes = new HashMap<>();
    
    private Scene sceneStart = null;
    private Scene sceneFinish = null;
    

    /**
     * Parses the script from a file and loads the scenes.
     *
     * @param file the script file to parse
     * @return a map of scenes
     * @throws IOException if an error occurs while reading the file
     */
    public Map<String, Scene> parseScript(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return parse(reader);
        }
    }

    /**
     * Parses the script from a string and loads the scenes.
     *
     * @param script the script content as a string
     * @return a map of scenes
     * @throws IOException if an error occurs while reading the string
     */
    public Map<String, Scene> parseScript(String script) throws IOException {
        try (BufferedReader reader = 
                new BufferedReader(
                        new StringReader(script)
                )
            ) {
            return parse(reader);
        }
    }
    
   

    /**
     * Parses the script using the provided BufferedReader.
     *
     * @param reader the BufferedReader to use for parsing
     * @return a map of scenes
     * @throws IOException if an error occurs while reading the script
     */
    private Map<String, Scene> parse(BufferedReader reader) throws IOException {
        String line;
        
        Scene currentScene = null;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            /// Scene and descriptions
            
            if (line.startsWith("# Scene: ")) {
                String sceneTitle = line.substring(9).trim();
                String sceneId = "scene-" + sceneTitle.toLowerCase().replace(" ", "-");
                currentScene = new Scene(sceneId, sceneTitle);
                scenes.put(sceneId, currentScene);
                // define the opening scene
                if(scenes.size() == 1){
                    sceneStart = currentScene;
                }
            } else if (line.startsWith("> ")) {
                if (currentScene != null) {
                    String text = line.substring(2);
                    if(currentScene.getDescription().isEmpty() == false){
                        text = currentScene.getDescription() + "\n\n" + text;
                    }
                    currentScene.addDescription(text);
                }
                
            /// Choices    
                
            } else if (line.startsWith("- [Take]")) {
                if (currentScene != null) {
                    String nameTitle = "Take";
                    String nameScene = line.substring(9).replace("(", "").replace(")", "").trim();
//                    currentScene.addChoice(nameTitle, "take-" + nameScene);
                }
            } else if (line.startsWith("- [Leave]")) {
                if (currentScene != null) {
                    String nameTitle = "Leave";
//                    currentScene.addChoice(nameTitle, "leave-" + currentScene.getId());
                }
//            else if (line.startsWith("- [Continue]")) {
//                if (currentScene != null) {
//                    String nameTitle = "Leave";
//                    currentScene.addChoice(nameTitle, "leave-" + currentScene.getId());
//                }
            } else if (line.startsWith("- [")) {
                // - [Investigate the side chamber](#scene-side-chamber)
                if (currentScene != null) {
                    int i = line.indexOf("]");
                    int x = line.indexOf("#");
                    String nameTitle = line.substring("- [".length(), i);
                    String nameScene = line.substring(x+1).replace("(", "").replace(")", "").trim();
//                    currentScene.addChoice(nameTitle, nameScene);
                }
                
//                
//                String nextScene = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
//                    currentScene.setNextScene(nextScene);
                
            // Items    
                
            } else if (line.startsWith("# Item: ")) {
                if (currentScene != null) {
                    String itemName = line.substring(8).trim();
                    String type = reader.readLine().trim().substring(6).trim();
                    String description = reader.readLine().trim().substring(12).trim();
                    currentScene.addItem(new Item());  // Adjusted to match Item class constructor
                }
            }
        }
        if (currentScene != null) {
            scenes.put(currentScene.getId(), currentScene);
            sceneFinish = currentScene;
        }
        return scenes;
    }

    public Map<String, Scene> getScenes() {
        return scenes;
    }

    public Scene getSceneStart() {
        return sceneStart;
    }

    public Scene getSceneFinish() {
        return sceneFinish;
    }

    public boolean isValid() {
        if(sceneStart == null || sceneFinish == null || scenes.isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    
    
}
