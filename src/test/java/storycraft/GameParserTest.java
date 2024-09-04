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
import online.nostrium.apps.storycraft.GameParser;
import online.nostrium.apps.storycraft.Item;
import online.nostrium.apps.storycraft.Scene;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class GameParserTest {

    private GameParser parser;

    @BeforeEach
    public void setUp() {
        parser = new GameParser();
    }

    @Test
    public void testParseScriptFromString() throws IOException {
        String script = """
                # Scene: Azurath Entrance
                > Welcome to the ancient ruins of Azurath. The air is thick with the scent of damp stone and decaying leaves.
                - [Take](item-sword)
                - [Leave](item-sword)
                - [Continue](scene-main-hall)
                
                # Item: Sword
                Type: Weapon
                Description: A sharp blade, well-suited for combat.
                
                # Scene: Main Hall
                > The main hall is vast, with towering pillars and a high, vaulted ceiling.
                """;

        Map<String, Scene> scenes = parser.parseScript(script);

        // Verify scenes are correctly parsed
        assertNotNull(scenes);
        assertEquals(2, scenes.size());

        // Check the first scene
        Scene entranceScene = scenes.get("azurath-entrance");
        assertNotNull(entranceScene);
        assertEquals("Welcome to the ancient ruins of Azurath. The air is thick with the scent of damp stone and decaying leaves.", entranceScene.getDescription());
        assertEquals(3, entranceScene.getChoices().size());
        assertEquals("scene-main-hall", entranceScene.getNextScene());

        // Check the second scene
        Scene mainHallScene = scenes.get("main-hall");
        assertNotNull(mainHallScene);
        assertEquals("The main hall is vast, with towering pillars and a high, vaulted ceiling.", mainHallScene.getDescription());

        // Verify item parsing
        assertEquals(1, entranceScene.getItems().size());
        Item sword = entranceScene.getItems().get(0);
        assertNotNull(sword);
        assertEquals("Sword", sword.getName());
        assertEquals("Weapon", sword.getType());
        assertEquals("A sharp blade, well-suited for combat.", sword.getDescription());
    }

//    @Test
//    public void testParseScriptFromFile() throws IOException {
//        File scriptFile = new File("src/test/resources/script.md"); // Adjust this path to your file location
//
//        Map<String, Scene> scenes = parser.parseScript(scriptFile);
//
//        // Verify scenes are correctly parsed
//        assertNotNull(scenes);
//        assertFalse(scenes.isEmpty());
//
//        // Check a scene from the file (you can add more specific checks based on the actual content)
//        Scene scene = scenes.get("azurath-entrance");
//        assertNotNull(scene);
//        assertTrue(scene.getDescription().contains("Welcome to the ancient ruins of Azurath"));
//    }
}
