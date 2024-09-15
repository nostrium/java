/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package storycraft;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import online.nostrium.apps.storycraft.Choice;
import online.nostrium.apps.storycraft.GamePlay;
import online.nostrium.apps.storycraft.GameScreen;
import online.nostrium.apps.storycraft.GameScreenCLI;
import online.nostrium.apps.storycraft.Scene;
import online.nostrium.main.Folder;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mvel2.MVEL;

/**
 * @author Brito
 * @date: 2024-09-06
 * @location: Germany
 */
public class MvelTest2 {

    public MvelTest2() {
    }

    @Test
    public void mvelTest() throws IOException {
        
        File folderBase = Folder.getFolderStories();
        File folderExamples = new File(folderBase, "examples");
        File file = new File(folderExamples, "FightExample.md");
        assertTrue(file.exists());
        String text = FileUtils.readFileToString(file, "UTF-8");
        
        // parse the script
        GameScreen screen = new GameScreenCLI();
        User user = UserUtils.createUserAnonymous();
        GamePlay game = new GamePlay(text, screen, user);
        
                
        // Initialize variables for Player A and Player B
        Map<String, Object> A = game.getPlayer().getAttributes();
        Map<String, Object> B = game.getOpponents().get("opponent-ogre").getAttributes();
        Map<String, Object> variables = new HashMap<>();

        
        // Add players to variables
        variables.put("A", A);
        variables.put("B", B);

        String script = game.getActions().getList().get(0).getScript();
        // Evaluate the script using MVEL
        MVEL.eval(script, variables);

        // Asserting the results (with int values)
        assertEquals(37, (int) B.get("Health"));  // After the attack, B's health should be 37
        assertEquals(0, A.get("Coins"));          // A did not win yet, coins should be 0

        // Output for debugging purposes
        System.out.println("B's Health: " + B.get("Health"));
        System.out.println("A's Coins: " + A.get("Coins"));

        // Modify B's health to test the winning scenario
        B.put("Health", -1);

        // Evaluate the script again after modifying B's health
        MVEL.eval(script, variables);

        // Asserting after B's health drops below 0
        assertEquals(-4, (int) B.get("Health")); 
        assertEquals(0, A.get("Coins"));
        
        
        
        // test that we get more options after finishing a choice
        Scene scene = game.getScene("scene-azurath-entrance");
        assertNotNull(scene);
        ArrayList<Choice> choices = scene.getChoices();
        assertNotNull(choices);
        Choice choice = choices.get(0);
        String nextActions = choice.getNextActions();
        assertEquals("#scene-victory", nextActions);
        
        
    }
}
