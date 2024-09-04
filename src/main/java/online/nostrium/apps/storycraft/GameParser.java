/*
 * Parses the game script and loads the scenes and items for the adventure, either from a file or a string.
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class GameParser {

    private final Map<String, Scene> scenes = new HashMap<>();

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
        try (BufferedReader reader = new BufferedReader(new StringReader(script))) {
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

            if (line.startsWith("# Scene: ")) {
                if (currentScene != null) {
                    scenes.put(currentScene.getId(), currentScene);
                }
                String sceneId = line.substring(9).trim().toLowerCase().replace(" ", "-");
                currentScene = new Scene(sceneId, "");
            } else if (line.startsWith("> ")) {
                if (currentScene != null) {
                    currentScene.setDescription(line.substring(2));
                }
            } else if (line.startsWith("- [Take]")) {
                if (currentScene != null) {
                    String itemName = line.substring(9).replace("(", "").replace(")", "").trim();
                    currentScene.addChoice("take-" + itemName);
                }
            } else if (line.startsWith("- [Leave]")) {
                if (currentScene != null) {
                    currentScene.addChoice("leave-" + currentScene.getId());
                }
            } else if (line.startsWith("- [Continue")) {
                if (currentScene != null) {
                    String nextScene = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                    currentScene.setNextScene(nextScene);
                }
            } else if (line.startsWith("# Item: ")) {
                if (currentScene != null) {
                    String itemName = line.substring(8).trim();
                    String type = reader.readLine().trim().substring(6).trim();
                    String description = reader.readLine().trim().substring(12).trim();
                    currentScene.addItem(new Item(itemName, description, type, 0, 0, 0, 0));  // Adjusted to match Item class constructor
                }
            }
        }
        if (currentScene != null) {
            scenes.put(currentScene.getId(), currentScene);
        }
        return scenes;
    }
}
