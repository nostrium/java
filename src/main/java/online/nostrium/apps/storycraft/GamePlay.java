/*
 * Handles the game loop and player interactions in the text-based adventure game.
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.util.ArrayList;
import java.util.Map;
import static online.nostrium.apps.storycraft.StoryUtils.normalize;
import online.nostrium.apps.storycraft.examples.StoryRandomItems;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class GamePlay {

    Player player;

    boolean valid = false;  // is the script valid?
    GameParser parser = null;
    final GameScreen screen;
    Map<String, Scene> scenes;
    int timeMessageDelay = 1;
            

    public GamePlay(String script, GameScreen screen, User user) {
        // parse the script
        String text = normalize(script);
        
        parser = new GameParser();
        this.screen = screen;
        parser.parseScript(text);
        valid = parser.isValid();
        scenes = parser.getScenes();
        // setup the Player
        player = new Player(user, screen);
        player.parse(text);
    }

      private void playScene(String sceneId) {
          Scene scene = scenes.get(sceneId);
          if(scene == null){
              return;
          }
          playScene(scene);
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

        // usually there is a choice or random choice
        Choice choice = null;

        // is there a random choice first?
        if (scene.getRandom().isEmpty() == false) {
            choice = playRandomChoice(scene);
        }

        // is there a choice to be made?
        if (scene.getChoices().isEmpty() == false) {
            choice = screen.performChoices(scene);
        }

        // process the type of choice that was made
        // since there was no valid choice, just exit then
        if(choice == null){
            return;
        }
        
        // is this a scene?
        if (choice.getLinkType() == LinkType.SCENE) {
            playScene(choice.link);
            return;
        }
        
        if (choice.getLinkType() == LinkType.ITEM) {
            Item item = parser.getItems().getItem(choice.link);
            player.addItem(item);
            playScene(scene);
            return;
        }

        // there are no choices, shall we go back?
        if (scene.getScenePrevious() != null) {
            playScene(scene.getScenePrevious());
        }
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

    private Choice playRandomChoice(Scene scene) {
        // select a random choice
        Choice choice = StoryUtils.selectRandomChoice(scene.getRandom());
        if (choice == null) {
            screen.writeln("Invalid choice");
            return null;
        }

        if (choice.linkType != LinkType.SCENE) {
            return choice;
        }

        // process a scene
        String sceneId = choice.getLink();
        if (scenes.containsKey(sceneId) == false) {
            screen.writeln("Invalid scene: " + sceneId);
            return null;
        }

        Scene nextScene = scenes.get(sceneId);
        if (nextScene == null) {
            screen.writeln("Scene not found: " + sceneId);
            return null;
        }

        // write the text
        if (scene.getTitleRandom() != null) {
            screen.writeln("");
            screen.writeln("");
            screen.writeln("     " + scene.getTitleRandom() + " ");
            screen.delay(timeMessageDelay);
            screen.writeln("\t\t" + choice.title);
            screen.delay(timeMessageDelay);
        }

        // define the way back
        nextScene.setScenePrevious(scene);
        // If the choice was a scene, play it
        playScene(nextScene);
        return choice;
    }

    public static void main(String[] args) {
        GameScreen screen = new GameScreenCLI();
        //GamePlay game = new GamePlay(StoryNavigateRooms.text, screen);
        User user = UserUtils.createUserAnonymous();
        user.setUsername("brito");
        GamePlay game = new GamePlay(StoryRandomItems.text, screen, user);
        game.play();
    }

    public Player getPlayer() {
        return player;
    }

    public Map<String, Opponent> getOpponents() {
        return parser.getOpponents();
    }
    
    public Actions getActions(){
        return parser.getActions();
    }

    public ArrayList<Item> getItems() {
        return this.parser.getItems().list;
    }

}
