/*
 * Handles the game loop and player interactions in the text-based adventure game.
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.util.Map;

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
        screen.writeln("---[ " + scene.getTitle() + " ]---");
        screen.writeln("");
        screen.writeln("/// " + scene.getDescription() + " ///");
        String nextSceneId = screen.performChoices(scene);
        // check if the scene exists
        Scene nextScene = parser.getScenes().get(nextSceneId);

        if (nextScene == null) {
            screen.writeln("Failed to find scene: " + nextSceneId);
            return;
        }
        // play the next scene
        playScene(nextScene);
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

    public static void main(String[] args) {
        GameScreen screen = new GameScreenCLI();
        GamePlay game = new GamePlay(StoryExample.text, screen);
        game.play();
    }

}
