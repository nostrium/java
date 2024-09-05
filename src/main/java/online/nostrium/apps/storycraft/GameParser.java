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

    private Scene sceneStart = null;
    private Scene sceneFinish = null;

    /**
     * Parses the script from a file and loads the scenes.
     *
     * @param file the script file to parse
     * @return a map of scenes
     * @throws IOException if an error occurs while reading the file
     */
    public boolean parseScript(File file) throws IOException {
        String text = FileUtils.readFileToString(file, "UTF-8");
        return parseScript(text);
    }

    /**
     * Parses the script from a string and loads the scenes.
     *
     * @param script the script content as a string
     */
    public boolean parseScript(String script) {
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

        return true;
    }

//    /**
//     * Parses the script using the provided BufferedReader.
//     *
//     * @param reader the BufferedReader to use for parsing
//     * @return a map of scenes
//     * @throws IOException if an error occurs while reading the script
//     */
//    private Map<String, Scene> parse(BufferedReader reader) throws IOException {
//        String line;
//
//        Scene currentScene = null;
//
//        while ((line = reader.readLine()) != null) {
//            line = line.trim();
//
//            /// Scene and descriptions
//            if (line.startsWith("# Scene: ")) {
//                String sceneTitle = line.substring(9).trim();
//                String sceneId = "scene-" + sceneTitle.toLowerCase().replace(" ", "-");
//                currentScene = new Scene(sceneId, sceneTitle);
//                scenes.put(sceneId, currentScene);
//                // define the opening scene
//                if (scenes.size() == 1) {
//                    sceneStart = currentScene;
//                }
//            } else if (line.startsWith("> ")) {
//                if (currentScene != null) {
//                    String text = line.substring(2);
//                    if (currentScene.getDescription().isEmpty() == false) {
//                        text = currentScene.getDescription() + "\n\n" + text;
//                    }
//                    currentScene.addDescription(text);
//                }
//
//                /// Choices    
//            } else if (line.startsWith("- [Take]")) {
//                if (currentScene != null) {
//                    String nameTitle = "Take";
//                    String nameScene = line.substring(9).replace("(", "").replace(")", "").trim();
//                    //currentScene.addChoice(nameTitle, "take-" + nameScene);
//                }
//            } else if (line.startsWith("- [Leave]")) {
//                if (currentScene != null) {
//                    String nameTitle = "Leave";
//                    //currentScene.addChoice(nameTitle, "leave-" + currentScene.getId());
//                }
////            else if (line.startsWith("- [Continue]")) {
////                if (currentScene != null) {
////                    String nameTitle = "Leave";
////                    currentScene.addChoice(nameTitle, "leave-" + currentScene.getId());
////                }
//            } else if (line.startsWith("- [")) {
//                // - [Investigate the side chamber](#scene-side-chamber)
//                if (currentScene != null) {
//                    int i = line.indexOf("]");
//                    int x = line.indexOf("#");
//                    String nameTitle = line.substring("- [".length(), i);
//                    String nameScene = line.substring(x + 1).replace("(", "").replace(")", "").trim();
////                    currentScene.addChoice(nameTitle, nameScene);
//                }
//
//                // Items    
//            } else if (line.startsWith("# Item: ")) {
//                if (currentScene != null) {
//                    String itemName = line.substring(8).trim();
//                    String type = reader.readLine().trim().substring(6).trim();
//                    String description = reader.readLine().trim().substring(12).trim();
//                    currentScene.addItem(new Item(itemName, description, type, 0, 0, 0, 0));  // Adjusted to match Item class constructor
//                }
//            }
//        }
//        if (currentScene != null) {
//            scenes.put(currentScene.getId(), currentScene);
//            sceneFinish = currentScene;
//        }
//        return scenes;
//    }
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

                // add this choice
                int y = line.indexOf("]");
                int x = line.indexOf("#");
                String choiceTitle = line.substring("- [".length(), y);
                String choiceLink = line.substring(x + 1).replace("(", "").replace(")", "").trim();

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

                Choice choice = new Choice(choiceTitle, choiceLink, linkType);
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
        String[] lines = randomBlock.split("\n");
        for (String line : lines) {
            // syntax: - 30% chance: [Fight a Skeleton Warrior](#scene-fight-skeleton)
            if (line.startsWith("- ") == false
                    || line.contains("%") == false) {
                continue;
            }
            // add this choice
            int y = line.indexOf("%");
            String chanceValueText = line
                    .substring("- [".length(), y);

            // try to convert the value
            int chanceValue = Integer.parseInt(chanceValueText);

            int z1 = line.indexOf(": [");
            int z2 = line.indexOf("]");
            String choiceTitle = line.substring(z1 + 3, z2);

            int x = line.indexOf("](#");
            String choiceLink = line.substring(x + 4)
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
    }

    private void parseItems(Scene scene, String sceneText) {
        ArrayList<String> itemBlocks = StoryUtils.getTextBlocks("## Item: ", sceneText);
        if (itemBlocks.isEmpty()) {
            return;
        }
        for (String itemBlock : itemBlocks) {
            Item item = parseItem(scene, itemBlock);
            if (item == null) {
                continue;
            }
            // add the item
            scene.addItem(item);
        }

    }

    private Item parseItem(Scene scene, String itemBlock) {

//        ## Item: Ancient Shield
//        Type: Shield  
//        Description: A shield from a bygone era, worn but sturdy.  
//        Defense Bonus: 5  
//        Durability: 20
        String name = null,
                id = null,
                description = null,
                type = null;

        HashMap<String, Integer> atts = new HashMap<>();

        String[] lines = itemBlock.split("\n");
        for (String line : lines) {
            // fixed values
            if (line.startsWith("## Item: ")) {
                name = line.substring("## Item: ".length());
                id = "item-" + name.toLowerCase().replace(" ", "-");
                continue;
            }
            if (line.startsWith("Description: ")) {
                description = line.substring("Description: ".length());
                continue;
            }
            if (line.startsWith("Type: ")) {
                type = line.substring("Type: ".length());
                continue;
            }
            // variable attributes
            if (line.contains(": ")) {
                int i = line.indexOf(": ");
                String key = line.substring(0, i);
                String valueText = line.substring(i + ": ".length());
                int value = Integer.parseInt(valueText);
                atts.put(key, value);
                continue;
            }

            // one empty line breaks the item data
            if (line.isEmpty()) {
                break;
            }
        }
        // the item needs to have the minimum sets of data
        if (name == null || description == null || type == null
                || atts.isEmpty()) {
            return null;
        }

        Item item = new Item(name, description, type);
        item.setId(id);
        item.addAttributes(atts);
        return item;
    }

}
