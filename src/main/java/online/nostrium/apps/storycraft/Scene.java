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
    private String description;       // The description of the scene
    private List<String> choices;     // The choices available to the player in this scene
    private List<Item> items;         // The items present in this scene
    private String nextScene;         // The next scene identifier after making a choice

    /**
     * Constructor for the Scene class.
     *
     * @param id          the identifier of the scene
     * @param description the description of the scene
     */
    public Scene(String id, String description) {
        this.id = id;
        this.description = description;
        this.choices = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void addChoice(String choice) {
        choices.add(choice);
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
}
