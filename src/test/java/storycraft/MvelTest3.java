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
public class MvelTest3 {

    public MvelTest3() {
    }

    @Test
    public void mvelTest() {
        
        // Script with simplified logic and no casting
        String script = """
            // calculate the attack and defense for each side
            AttackPowerA = A['Attack'] + (A['Experience'] / (A['Attack'] * 0.5)); 
            DefendPowerA = A['Defense'] + (A['Experience'] / (A['Defense'] * 0.5));
            AttackPowerB = B['Attack'] + (B['Experience'] / (B['Attack'] * 0.5)); 
            DefendPowerB = B['Defense'] + (B['Experience'] / (B['Defense'] * 0.5));
                        
            // run the attack on both sides         
            A['Health'] = A['Health'] - Math.max(0, AttackPowerB - DefendPowerA);
            B['Health'] = B['Health'] - Math.max(0, AttackPowerA - DefendPowerB);
                
            // provide the output result
            output = A['Health'] < 0 ? 'lose' : (B['Health'] < 0 ? 'win' : 'continue');
            """;

        // Initialize variables for Player A and Player B
        Map<String, Object> A = new HashMap<>();
        Map<String, Object> B = new HashMap<>();
        Map<String, Object> variables = new HashMap<>();

        // Player A's attributes (keep everything as integers)
        A.put("Attack", 40);
        A.put("Experience", 30);
        A.put("Health", 40);
        A.put("Defense", 5);
        A.put("Coins", 0);

        // Player B's attributes (keep everything as integers)
        B.put("Attack", 50);
        B.put("Experience", 30);
        B.put("Health", 40);
        B.put("Defense", 5);
        
        // Add players to variables
        variables.put("A", A);
        variables.put("B", B);

        // Evaluate the script using MVEL
        MVEL.eval(script, variables);
        
        String output = (String) variables.get("output");
        assertEquals("continue", output);

        // Asserting the results (with int values)
        assertEquals(16, (int) B.get("Health"));  // After the attack, B's health should be 37
        assertEquals(6, (int) A.get("Health"));  // After the attack, B's health should be 37
//        assertEquals(0, A.get("Coins"));          // A did not win yet, coins should be 0

        // Output for debugging purposes
        System.out.println("B's Health: " + B.get("Health"));
        System.out.println("A's Coins: " + A.get("Coins"));

        // Modify B's health to test the winning scenario
        B.put("Health", -1);

        // Evaluate the script again after modifying B's health
        MVEL.eval(script, variables);

        // Asserting after B's health drops below 0
        assertTrue((int) B.get("Health") < 0);    // B's health remains at -1
    }
}
