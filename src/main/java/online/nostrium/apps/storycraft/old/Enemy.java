/*
 * Something that is attacking the users
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.storycraft.old;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class Enemy {

    String name;
    int health;
    int attack;
    int defense;
    int experience;

    public Enemy(String name, int health, int attack, int defense, int experience) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.experience = experience;
    }
}
