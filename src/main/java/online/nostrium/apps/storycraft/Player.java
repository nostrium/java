/*
 * The Player itself
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import online.nostrium.user.User;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class Player extends GameThing {

    User user;
    final GameScreen screen;
    List<Item> inventory;
    String title = null;
    int maxSize = 10;

    final String keyword = "Player";
    final String anchor = "# " + keyword;

    public Player(User user, GameScreen screen) {
        this.screen = screen;
        this.user = user;
        this.inventory = new ArrayList<>();
    }

    public void addItem(Item item) {
        if (inventory.size() > maxSize) {
            screen.writeln("You inventory is full! Please remove some items");
            return;
        }
        screen.writeln("Added item to inventory: " + item.getName());
        inventory.add(item);
        screen.delay(1);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    public void listInventory() {
        System.out.println("Inventory:");
        for (Item item : inventory) {
            screen.writeln("- " + item.name);
        }
    }

    @Override
    public String toString() {
        return "Player{"
                + ", inventory=" + inventory.size() + " items"
                + '}';
    }

    /**
     * Parse player info into this object
     *
     * @param text
     */
    public void parse(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }

        String block = StoryUtils.getTextBlock(anchor, text);
        if (block == null || block.isEmpty()) {
            return;
        }

        String[] lines = block.split("\n");
        for (String line : lines) {
            // was a title defined?
            if (line.startsWith(anchor + ": ")) {
                title = line.substring((anchor + ": ").length());
            }

            // variable attributes
            if (line.startsWith("- ") && line.contains(": ")) {
                int i = line.indexOf(": ");
                String key = line.substring("- ".length(), i);
                String valueText = line.substring(i + ": ".length());
                attributes.put(key, valueText);
                continue;
            }

            // one empty line breaks the item data
            if (line.isEmpty()) {
                break;
            }

        }

    }

    @Override
    protected boolean processedSpecificLine(Scene scene, String line, HashMap<String, String> atts) {
        return false;
    }

    @Override
    protected void processedSpecificBlock(Scene scene, String textBlock) {
    }

}