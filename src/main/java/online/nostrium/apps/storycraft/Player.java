/*
 * The Player itself
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
public class Player {

    int health;
    int attack;
    int defense;
    int experience;
    int level;
    List<Item> inventory;

    public Player(int health, int attack, int defense, int experience, int level) {
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.experience = experience;
        this.level = level;
        this.inventory = new ArrayList<>();
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    public void listInventory() {
        System.out.println("Inventory:");
        for (Item item : inventory) {
            System.out.println("- " + item.name);
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "health=" + health +
                ", attack=" + attack +
                ", defense=" + defense +
                ", experience=" + experience +
                ", level=" + level +
                ", inventory=" + inventory.size() + " items" +
                '}';
    }
}
