/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package storycraft;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mvel2.MVEL;

/**
 * @author Brito
 * @date: 2024-09-06
 * @location: Germany
 */
public class MvelTest {

    public MvelTest() {
    }

    @Test
    public void mvelTest() {
        
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

        // Initialize variables for Player A and Player B
        Map<String, Object> A = new HashMap<>();
        Map<String, Object> B = new HashMap<>();
        Map<String, Object> variables = new HashMap<>();

        // Player A's attributes (keep everything as integers)
        A.put("Attack", 10);
        A.put("Experience", 30);
        A.put("Health", 50);
        A.put("Coins", 0);

        // Player B's attributes (keep everything as integers)
        B.put("Defense", 5);
        B.put("Experience", 20);
        B.put("Health", 40);

        // Add players to variables
        variables.put("A", A);
        variables.put("B", B);

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
