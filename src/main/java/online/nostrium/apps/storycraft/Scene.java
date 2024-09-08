/*
 * Represents a scene in the text-based adventure game.
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.util.ArrayList;
import static online.nostrium.apps.storycraft.StoryUtils.getMatchingLines;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class Scene {

    private String id;                // The identifier of the scene
    private String title;             // Human readable title 
    private String intro;             // description of the action
    private String description;       // description of the scene
    private String titleRandom = null; // sometimes random can have titles
    
    private final ArrayList<Choice> choices;// choices available to the player in this scene
    private final ArrayList<Choice> random; // random choices available
    
    private String nextScene;         // next scene identifier after making a choice
    private Scene scenePrevious;      // used when no other path is available

    /**
     * Constructor for the Scene class.
     *
     */
    public Scene() {
        this.choices = new ArrayList<>();
        this.random = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        if (description == null) {
            return "";
        }
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addDescription(String text) {
        if (description != null && description.isEmpty() == false) {
            description = description + "\n\n" + text;
            return;
        }
        this.description = text;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public ArrayList<Choice> getChoices() {
        return choices;
    }

    public void addChoice(Choice choice) {
        choices.add(choice);
    }
    
    public ArrayList<Choice> getRandom() {
        return random;
    }

    public void addRandom(Choice choice) {
        random.add(choice);
    }

    
    public String getNextScene() {
        return nextScene;
    }

    public void setNextScene(String nextScene) {
        this.nextScene = nextScene;
    }

    public String getTitle() {
        return title;
    }

    public void setScenePrevious(Scene scene) {
        scenePrevious = scene;
    }

    public Scene getScenePrevious() {
        return scenePrevious;
    }

    public String getTitleRandom() {
        return titleRandom;
    }

    public void setTitleRandom(String titleRandom) {
        this.titleRandom = titleRandom;
    }
    
    public void parse(String sceneText) {
//        
//    # Scene: Azurath Entrance
//    > Welcome to the ancient ruins of Azurath. The air is thick with the scent of damp stone and decaying leaves. You are a treasure hunter, drawn by the legends of untold riches hidden within.
//    > The entrance to the ruins is overgrown with vines. Faded carvings on the stone walls hint at the once-great civilization that lived here.
//
        // get the first line
        int i = sceneText.indexOf("\n");
        String anchor = "# Scene: ";
        title = sceneText.substring(anchor.length(), i);
        id = "scene-" + title.toLowerCase().replace(" ", "-");
        
        // get the description lines
        ArrayList<String> descLines = getMatchingLines("> ", sceneText);
        for (String descLine : descLines) {
            addDescription(descLine.substring(2));
        }

        // get the intro
        String introBlock = StoryUtils.getTextBlock("## Intro", sceneText);
        if (introBlock.isEmpty() == false) {
            // skip the first line
            i = introBlock.indexOf("\n");
            String intro = introBlock.substring(i + 1);
            setIntro(intro);
        }

        parseChoices(this, sceneText);
        parseChoicesRandom(this, sceneText);
        
        return;
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

    public boolean isValid() {
        return choices.isEmpty() == false
                || random.isEmpty() == false;
    }

}
