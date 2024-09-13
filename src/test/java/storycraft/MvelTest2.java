/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package storycraft;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import online.nostrium.apps.storycraft.GamePlay;
import online.nostrium.apps.storycraft.GameScreen;
import online.nostrium.apps.storycraft.GameScreenCLI;
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
        
        /*
        // Script with simplified logic and no casting
        String script = """
            AttackPower = A['Attack'] + (A['Experience'] / (A['Attack'] * 0.5)); 
            DefendPower = B['Defense'] + (B['Experience'] / (B['Defense'] * 0.5));
            if (B['Health'] > 0) {B['Health'] = B['Health'] - Math.max(0, AttackPower - DefendPower);}
            if (A['Health'] < 0) { A['Coins'] = 0; } // lose all coins
            if (B['Health'] < 0) { A['Coins'] = A['Coins'] + 10; } // win coins
            // write an output
            output = A['Health'] < 0 ? '#scene-end' : (B['Health'] < 0 ? 'You have won!' : null);
            """;
*/
        
                
        // Initialize variables for Player A and Player B
        Map<String, Object> A = game.getPlayer().getAttributes();
        Map<String, Object> B = game.getOpponents().get("opponent-ogre").getAttributes();
        Map<String, Object> variables = new HashMap<>();

        
        // Player A's attributes (keep everything as integers)
//        A.put("Health", 50);
//        A.put("Attack", 10);
//        A.put("Defense", 5);
//        A.put("Experience", 30);
//        A.put("Coins", 0);

        // Player B's attributes (keep everything as integers)
//        B.put("Health", 40);
//        B.put("Attack", 10);
//        B.put("Defense", 5);
//        B.put("Experience", 20);
        
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
        assertEquals(-1, (int) B.get("Health"));    // B's health remains at -1
        assertEquals(10, A.get("Coins"));           // A wins and gains 10 coins
    }
}
