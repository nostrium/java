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
import static online.nostrium.apps.storycraft.StoryUtils.getMatchingLines;
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
    private final Map<String, Opponent> opponents = new HashMap<>();

    private Scene sceneStart = null;
    private Scene sceneFinish = null;
    private Actions actions = new Actions();
    
    
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
            Scene scene = parseScene(sceneText);
            scenesFound.add(scene);
            this.scenes.put(scene.getId(), scene);
        }
        // define the first and last screens
        this.sceneStart = scenesFound.get(0);
        this.sceneFinish = scenesFound.get(scenesFound.size() - 1);
        
        // get the actions
        actions.parse(text);
        
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

    private Scene parseScene(String sceneText) {
//        
//    # Scene: Azurath Entrance
//    > Welcome to the ancient ruins of Azurath. The air is thick with the scent of damp stone and decaying leaves. You are a treasure hunter, drawn by the legends of untold riches hidden within.
//    > The entrance to the ruins is overgrown with vines. Faded carvings on the stone walls hint at the once-great civilization that lived here.
//
//    ## Intro
//    You step into the ruins, the sound of your footsteps echoing off the stone walls. The air is cooler inside, and a sense of foreboding settles over you.
// 
        // get the first line
        int i = sceneText.indexOf("\n");
        String anchor = "# Scene: ";
        String title = sceneText.substring(anchor.length(), i);
        String id = "scene-" + title.toLowerCase().replace(" ", "-");
        Scene scene = new Scene(id, title);

        // get the description lines
        ArrayList<String> descLines = getMatchingLines("> ", sceneText);
        for (String descLine : descLines) {
            scene.addDescription(descLine.substring(2));
        }

        // get the intro
        String introBlock = StoryUtils.getTextBlock("## Intro", sceneText);
        if (introBlock.isEmpty() == false) {
            // skip the first line
            i = introBlock.indexOf("\n");
            String intro = introBlock.substring(i + 1);
            scene.setIntro(intro);
        }

        parseChoices(scene, sceneText);
        parseChoicesRandom(scene, sceneText);
        parseItems(scene, sceneText);
        parseOpponents(scene, sceneText);

        return scene;
    }

    private void parseChoices(Scene scene, String sceneText) {
//    ## Choices:
//    - [Explore the main hall](#scene-main-hall)
//    - [Investigate the side chamber](#scene-side-chamber)
//    - [Leave the ruins](#scene-leave-ruins)

        // get the choices
        String choiceBlock = StoryUtils.getTextBlock("## Choices:", sceneText);
        if (choiceBlock.isEmpty() == false) {
            String[] lines = choiceBlock.split("\n");
            for (String line : lines) {
                if (line.startsWith("- [") == false) {
                    continue;
                }
//                ## Choices:
//                - [Take the artifact](#scene-take-artifact)
//                - [Leave the artifact](#scene-leave-artifact)

                // special situation to leave the menu
                if (line.startsWith("- [Leave]")) {
                    String choiceTitle = "Leave";
                    int f = line.indexOf("#");
                    String choiceLink = line.substring(f + 1)
                            .replace("(", "")
                            .replace(")", "")
                            .trim();
                    Choice choice = new Choice(
                            choiceTitle,
                            choiceLink,
                            LinkType.LEAVE);
                    scene.addChoice(choice);
                    scene.setNextScene(choiceLink);
                    continue;
                }

                Choice choice = Choice.parse(line);
                scene.addChoice(choice);
            }
        }
    }

    private void parseChoicesRandom(Scene scene, String sceneText) {
        // get the choices
        String randomBlock = StoryUtils.getTextBlock("## Random:", sceneText);
        if (randomBlock.isEmpty()) {
            return;
        }
        String titleRandom = null;
        
        String[] lines = randomBlock.split("\n");
        for (String line : lines) {
            // ## Random: You look around and...
            if (line.startsWith("## Random: ")) {
                titleRandom = line.substring("## Random: ".length());
                continue;
            }
            // syntax: - 30% chance: [Fight a Skeleton Warrior](#scene-fight-skeleton)
            if (line.startsWith("- ") == false
                    || line.contains("%") == false) {
                continue;
            }
            // add this choice
            int posPercent = line.indexOf("%");
            String chanceValueText = line
                    .substring("- ".length(), posPercent);

            // try to convert the value
            int chanceValue = Integer.parseInt(chanceValueText);

            int t1 = line.indexOf("[");
            int t2 = line.indexOf("]");
            String choiceTitle = line.substring(t1+1, t2);

            int x = line.indexOf("](#");
            String choiceLink = line.substring(x + 3)
                    .replace("(", "")
                    .replace(")", "")
                    .trim();

            // what kind of choice type?
//              - [Take](#item-rusty-sword)
//              - [Lose](#item-coins-5-10)
//              - [Continue exploring the hall](#scene-continue-hall)
            LinkType linkType;
            if (choiceLink.startsWith("item-")) {
                linkType = LinkType.ITEM;
            } else if (choiceLink.startsWith("scene-")) {
                linkType = LinkType.SCENE;
            } else {
                linkType = LinkType.OTHER;
            }

            Choice choice = new Choice(
                    choiceTitle, choiceLink, chanceValue, linkType);
            scene.addRandom(choice);
        }
        // add the title
        if(titleRandom != null){
            scene.setTitleRandom(titleRandom);
        }
    }

    private void parseItems(Scene scene, String sceneText) {
        String anchor = "## Item: ";
        String anchorId = "item-";
        ArrayList<String> itemBlocks = StoryUtils.getTextBlocks(anchor, sceneText);
        if (itemBlocks.isEmpty()) {
            return;
        }
        for (String itemBlock : itemBlocks) {
            Item item = new Item();
            boolean result = item.parse(scene, itemBlock, anchor, anchorId);
            if (result == false) {
                continue;
            }
            // add the item
            scene.addItem(item);
        }
    }

   

    private void parseOpponents(Scene scene, String sceneText) {
        String anchor = "## Opponent: ";
        String anchorId = "opponent-";
        ArrayList<String> blocks = StoryUtils.getTextBlocks(anchor, sceneText);
        if (blocks.isEmpty()) {
            return;
        }
        for (String itemBlock : blocks) {
            Opponent op = new Opponent();
            boolean result = op.parse(scene, itemBlock, anchor, anchorId);
            if (result == false) {
                continue;
            }
            // add the item
            scene.addOpponent(op);
            opponents.put(op.id, op);
        }
    }

    public Map<String, Opponent> getOpponents() {
        return opponents;
    }
    
    

}
