/*
 * Handles the game loop and player interactions in the text-based adventure game.
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.util.Map;
import online.nostrium.apps.storycraft.examples.StoryRandomRooms;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class GamePlay {

    boolean valid = false;  // is the script valid?
    GameParser parser = null;
    final GameScreen screen;
    Map<String, Scene> scenes;

    public GamePlay(String script, GameScreen screen) {
        // parse the script
        parser = new GameParser();
        this.screen = screen;
        parser.parseScript(script); // Parse from a string for this example
        valid = parser.isValid();
        scenes = parser.getScenes();
    }

    /**
     * Plays a scene and present the options
     *
     * @param scene
     */
    private void playScene(Scene scene) {
        screen.writeln("");
        screen.writeln("---[ " + scene.getTitle() + " ]---");
        screen.writeln("");
        screen.writeln("/// " + scene.getDescription() + " ///");

        // last scene is the end
        if (scene == this.parser.getSceneFinish()) {
            return;
        }

        // is there a random choice first?
        if (scene.getRandom().isEmpty() == false) {
            playRandomChoice(scene);
            return;
        }

        // is there a choice to be made?
        if (scene.getChoices().isEmpty() == false) {
            playChoice(scene);
            return;
        }

        // there are no choices, shall we go back?
    }

    public boolean play() {
        if (valid == false) {
            return false;
        }
        playScene(parser.getSceneStart());
        return true;
    }

    public boolean isValid() {
        return valid;
    }

    public Map<String, Scene> getScenes() {
        return scenes;
    }

    public Scene getScene(String sceneName) {
        String id = sceneName;
        if (sceneName.startsWith("#")) {
            id = StoryUtils.convertSceneTitleToId(sceneName);
        }
        return scenes.get(id);
    }

    public void playChoice(Scene scene) {
        if (scene.getChoices().isEmpty()) {
            return;
        }

        Choice choice = screen.performChoices(scene);

        // check if the scene exists
        Scene nextScene = null;
        if (choice != null) {
            nextScene = parser.getScenes().get(choice.link);
        }

        if (nextScene == null) {
            screen.writeln("Failed to find scene: " + choice.link);
            return;
        }

        // play the next scene
        nextScene.setScenePrevious(scene);
        playScene(nextScene);
    }

    private void playRandomChoice(Scene scene) {
        // select a random choice
        Choice choice = StoryUtils.selectRandomChoice(scene.getRandom());
        if (choice == null) {
            screen.writeln("Invalid choice");
            return;
        }
        String sceneId = choice.getLink();
        if (scenes.containsKey(sceneId) == false) {
            screen.writeln("Invalid scene: " + sceneId);
            return;
        }
        Scene nextScene = scenes.get(sceneId);
        if (nextScene == null) {
            screen.writeln("Scene not found: " + sceneId);
            return;
        }
        
        // write the text
        if(scene.getTitleRandom() != null){
            int time = 2;
            screen.writeln("");
            screen.writeln("");
            screen.writeln("     " + scene.getTitleRandom() + " ");
            screen.delay(time);
            screen.writeln("\t\t" + choice.title);
            screen.delay(time);
        }
        
        
        // define the way back
        nextScene.setScenePrevious(scene);
        playScene(nextScene);
    }

    

    public static void main(String[] args) {
        GameScreen screen = new GameScreenCLI();
        //GamePlay game = new GamePlay(StoryNavigateRooms.text, screen);
        GamePlay game = new GamePlay(StoryRandomRooms.text, screen);
        game.play();
    }
    
}
