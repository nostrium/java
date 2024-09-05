/*
 * Represents a scene in the text-based adventure game.
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.util.ArrayList;
import java.util.List;

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
    private ArrayList<Choice> choices;// choices available to the player in this scene
    private String titleRandom = null; // sometimes random can have titles
    private ArrayList<Choice> random; // random choices available
    private List<Opponent> opponents; // opponents inside the scene
    private List<Item> items;         // items present in this scene
    private String nextScene;         // next scene identifier after making a choice
    private Scene scenePrevious;      // used when no other path is available

    /**
     * Constructor for the Scene class.
     *
     * @param id the identifier of the scene
     * @param title human readable title
     */
    public Scene(String id, String title) {
        this.id = id;
        this.title = title;
        this.choices = new ArrayList<>();
        this.random = new ArrayList<>();
        this.items = new ArrayList<>();
        this.opponents = new ArrayList<>();
    }

    public String getId() {
        return id;
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

    public void setItems(List<Item> items) {
        this.items = items;
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


    public void addItem(Item item) {
        items.add(item);
    }

    public void addOpponent(Opponent op) {
        opponents.add(op);
    }
    
    public List<Item> getItems() {
        return items;
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

    public List<Opponent> getOpponents() {
        return opponents;
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


}
