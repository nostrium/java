/*
 * The Player itself
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft;

import java.util.ArrayList;
import java.util.List;
import online.nostrium.user.User;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class Player {

    User user;
    final GameScreen screen;
    List<Item> inventory;
    int maxSize = 10;

    public Player(User user, GameScreen screen) {
        this.screen = screen;
        this.user = user;
        this.inventory = new ArrayList<>();
    }

    public void addItem(Item item) {
        if(inventory.size() > maxSize){
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
}
