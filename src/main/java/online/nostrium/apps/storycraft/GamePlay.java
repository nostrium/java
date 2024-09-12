/*
 * Handles the game loop and player interactions in the text-based adventure game.
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import static online.nostrium.apps.storycraft.StoryUtils.normalize;
import online.nostrium.main.Folder;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;
import org.apache.commons.io.FileUtils;

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
        if (scene == null) {
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
        if (scene.sameAs(parser.getSceneFinish())) {
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
        if (choice == null) {
            return;
        }

        // is this a scene?
        if (choice.getLinkType() == LinkType.SCENE) {
            Scene nextScene = scenes.get(choice.getLink());
            if (nextScene != null) {
                nextScene.setScenePrevious(scene);
            }
            // jump to the next scene
            playScene(choice.link);
            return;
        }

        if (choice.getLinkType() == LinkType.ITEM) {
            Item item = parser.getItems().getItem(choice.link);
            player.addItem(item);
            //player.writeStatus();
            // play the scene again
            playScene(scene);
            return;
        }

        if (choice.getLinkType() == LinkType.FIGHT) {
            fight(choice, scene);
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
        return choice;
    }

    public Player getPlayer() {
        return player;
    }

    public Map<String, Opponent> getOpponents() {
        return parser.getOpponents();
    }

    public Actions getActions() {
        return parser.getActions();
    }

    public ArrayList<Item> getItems() {
        return this.parser.getItems().list;
    }

    /**
     * Start a fight between the player and an opponent
     *
     * @param choice
     * @param scene
     */
    private void fight(Choice choice, Scene scene) {

        Player A = getPlayer();
        Opponent B = parser.getOpponent(choice);

        if (B == null) {
            screen.writeln("The opponent could not be found! :-(");
            screen.writeln("Opponent: " + choice.link);
            screen.delay(1);
            // play the scene again
            playScene(scene);
            return;
        }
        
        String text = StoryUtils.showStats(A, B);
        screen.writeln(text);
        screen.writeln("");
        
        // the opponent exists and can fight
        screen.writeln("The fight begins!");
        screen.delay(1);

        // loop to repeat the game until the stop
        // conditions are met
        boolean continueEvent = true;
        while (continueEvent) {
            continueEvent = fightAction(A, B);
        }

    }

    /**
     * 
     * @param A Player
     * @param B Opponent
     * @return true to continue fighting, false to exit
     */
    private boolean fightAction(Player A, Opponent B) {
        // get the actions to play
        String[] nextSteps = null;
        String[] actionsAvailable = B.getActions();
        if (actionsAvailable == null || actionsAvailable.length == 0) {
            screen.writeln("No actions available for this opponent");
            screen.writeln("Opponent: " + B.getName());
            screen.delay(1);
            return false;
        }

        // get the selected action
        String actionSelected = screen.performChoices(actionsAvailable);

        if (actionSelected == null) {
            screen.writeln("No action was selected");
            screen.delay(1);
            return false;
        }

        screen.writeln("Chosen action: " + actionSelected);
        screen.delay(1);

        Action action = this.getActions().get(actionSelected);
        if (action == null) {
            screen.writeln("Expected action was NOT found");
            screen.writeln("Action: " + actionSelected);
            screen.delay(1);
            return false;
        }

        // player attacks opponent
        screen.writeln("You attack " + B.name);
        action.processAction(A, B);
        screen.delay(1);

        nextSteps = action.canStop(A, B);
        if (runActions(nextSteps)) {
            return false;
        }

        // opponent attacks player
        screen.writeln(B.name + " attacks you");
        action.processAction(B, A);

        nextSteps = action.canStop(A, B);
        if (runActions(nextSteps)) {
            return false;
        }

        // keep looping
        return true;
    }

    /**
     * Run the statements after an If condition has occurred
     * @param nextSteps
     * @return false to continue running
     */
    public boolean runActions(String[] nextSteps) {
        if(nextSteps == null || nextSteps.length == 0){
            return false;
        }
        
        for(String step : nextSteps){
            boolean stopRunning = runAction(step);
            if(stopRunning){
                return true;
            }
        }
        
        return false;
    }

    private boolean runAction(String step) {
        String stepLowercase = step.toLowerCase();
        // write something on the screen
        if(stepLowercase.startsWith("write")){
            String text = step.substring("write ".length()).trim();
            if(text.startsWith("\"") && text.endsWith("\"")){
                text = text.substring(1, text.length() -1);
            }
            screen.writeln(text);
            screen.delay(1);
            // stop running?
            return false;
        }
        
        // support scenes
        if(stepLowercase.startsWith("#scene")){
            // remove the #
            step = step.substring(1);
            playScene(step);
            // stop running?
            return true;
        }
        
        // support items
        if(stepLowercase.startsWith("#item")){
            Item item = parser.getItems().getItem(step);
            if(item != null){
                player.addItem(item);
            }
            // stop running?
            return true;
        }
        // stop running?
        return false;
    }

    
    
    public static void main(String[] args) throws IOException {
        GameScreen screen = new GameScreenCLI();
        User user = UserUtils.createUserAnonymous();
        user.setUsername("brito");
        File folderBase = Folder.getFolderStories();
        File folderExamples = new File(folderBase, "examples");
        File file = new File(folderExamples, "FightExample.md");
        String text = FileUtils.readFileToString(file, "UTF-8");
        
        //GamePlay game = new GamePlay(StoryNavigateRooms.text, screen, user);
        //GamePlay game = new GamePlay(StoryRandomRooms.text, screen, user);
        GamePlay game = new GamePlay(text, screen, user);
        game.play();
    }

    
}
