/*
 * Parses the game script and loads the scenes and items
 * for the adventure, either from a file or a string.
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static online.nostrium.apps.storycraft.StoryUtils.getTextBlocks;
import static online.nostrium.apps.storycraft.StoryUtils.normalize;
import org.apache.commons.io.FileUtils;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class GameParser {

    private final Map<String, Scene> scenes = new HashMap<>();
    private final Items items = new Items();
    private final Opponents opponents = new Opponents();

    private Scene sceneStart = null;
    private Scene sceneFinish = null;
    private final Actions actions = new Actions();
    
    
    /**
     * Parses the script from a file and loads the scenes.
     *
     * @param file the script file to parse
     * @throws IOException if an error occurs while reading the file
     */
    public void parseScript(File file) throws IOException {
        String text = FileUtils.readFileToString(file, "UTF-8");
        parseScript(text);
    }

    /**
     * Parses the script from a string and loads the scenes.
     *
     * @param script the script content as a string
     */
    public void parseScript(String script) {
        String text = normalize(script);
        ArrayList<String> sceneTexts = getTextBlocks("# Scene: ", text);
        ArrayList<Scene> scenesFound = new ArrayList<>();
        for (String sceneText : sceneTexts) {
            Scene scene = new Scene();
            scene.parse(sceneText);
//            if(scene.isValid() == false){
//                continue;
//            }
            scenesFound.add(scene);
            this.scenes.put(scene.getId(), scene);
        }
        // define the first and last screens
        if(this.scenes.size() > 0){
            this.sceneStart = scenesFound.get(0);
            this.sceneFinish = scenesFound.get(scenesFound.size() - 1);
        }
        
        // get the actions
        actions.parse(text);
        items.parse(text);
        opponents.parse(text);
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

    public Actions getActions() {
        return actions;
    }
    
    public boolean isValid() {
        return !(sceneStart == null || sceneFinish == null || scenes.isEmpty());
    }

    public Items getItems() {
        return items;
    }

    public Map<String, Opponent> getOpponents() {
        return opponents.getOpponents();
    }
    
}
