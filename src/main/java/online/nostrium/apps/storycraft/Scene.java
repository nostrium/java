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
    private ArrayList<Choice> choices;// The choices available to the player in this scene
    private ArrayList<Choice> random; // The crandom hoices available
    private List<Item> items;         // The items present in this scene
    private String nextScene;         // The next scene identifier after making a choice

    /**
     * Constructor for the Scene class.
     *
     * @param id the identifier of the scene
     */
    public Scene(String id, String title) {
        this.id = id;
        this.title = title;
        this.choices = new ArrayList<>();
        this.items = new ArrayList<>();
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

}
