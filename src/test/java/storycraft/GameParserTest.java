/*
 * Unit tests for the GameParser class, which parses the game script and loads the scenes and items.
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package storycraft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import online.nostrium.apps.storycraft.GamePlay;
import online.nostrium.apps.storycraft.GameScreen;
import online.nostrium.apps.storycraft.GameScreenCLI;
import online.nostrium.apps.storycraft.Item;
import online.nostrium.apps.storycraft.Scene;
import online.nostrium.apps.storycraft.StoryExample;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class GameParserTest {

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testParseScriptFromString() throws IOException {
        
       String script = """
                # Scene: Azurath Entrance
                > Welcome to the ancient ruins of Azurath. The air is thick with the scent of damp stone and decaying leaves.
                
                ## Choices:
                - [Take](#item-sword)
                - [Leave](#scene-main-hall)
                
                ## Item: Sword
                Type: Weapon  
                Description: A worn and damaged sword, but it still has some fight left in it.
                Attack Bonus: 3  
                Durability: 10
                
                # Scene: Main Hall
                > The main hall is vast, with towering pillars and a high, vaulted ceiling.
                       
                ## Item: Ancient Shield
                Type: Shield  
                Description: A shield from a bygone era, worn but sturdy.  
                Defense Bonus: 5  
                Durability: 20
                        
                ## Random:
                - 30% chance: [Fight a Skeleton Warrior](#scene-fight-skeleton)
                - 30% chance: [Find a hidden alcove with a shield](#scene-find-shield)
                - 20% chance: [Nothing happens](#scene-nothing-happens)
                - 20% chance: [Find a pot of coins](#scene-find-coins)        
                        
                """;

        GameScreen screen = new GameScreenCLI();
        GamePlay game = new GamePlay(script, screen);
        
        assertTrue(game.isValid());
        
        Map<String, Scene> scenes = game.getScenes();
        
        // Verify scenes are correctly parsed
        assertNotNull(scenes);
        assertEquals(2, scenes.size());

        // Check the first scene
        Scene entranceScene = scenes.get("scene-azurath-entrance");
        assertNotNull(entranceScene);
        assertEquals("Welcome to the ancient ruins of Azurath. "
                + "The air is thick with the scent of damp stone and "
                + "decaying leaves.", entranceScene.getDescription());
        assertEquals(2, entranceScene.getChoices().size());
        
        
        assertEquals("scene-main-hall", entranceScene.getNextScene());

        // Check the second scene
        Scene mainHallScene = scenes.get("scene-main-hall");
        assertNotNull(mainHallScene);
        assertEquals("The main hall is vast, with towering pillars and a high, vaulted ceiling.", mainHallScene.getDescription());

        // Verify item parsing
        assertEquals(1, entranceScene.getItems().size());
        Item sword = entranceScene.getItems().get(0);
        assertEquals(4, entranceScene.getRandom().size());
        assertNotNull(sword);
        assertEquals("Sword", sword.getName());
        assertEquals("Weapon", sword.getType());
        assertEquals("A worn and damaged sword, but it still has some fight left in it.", sword.getDescription());
    }

}
