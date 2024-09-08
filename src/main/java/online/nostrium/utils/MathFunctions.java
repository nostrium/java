/*
 *  Functions related to numbers and math
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.utils;

import java.util.Random;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

/**
 * @Date: 2024-08-25
 * @Place: Germany
 * @author brito
 */
public class MathFunctions {

    public static int getRandomIntInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
    
    // Static method to evaluate an expression and return a long result
    public static long evaluateExpression(String expression) throws Exception {
        
        // Define max() function, called "chooseGreater" to ease understanding
        Function maxFunction = new Function("chooseGreater", 2) {
            @Override
            public double apply(double... args) {
                return Math.max(args[0], args[1]);
            }
        };
        
        // Use exp4j to parse and evaluate the expression
        Expression exp = new ExpressionBuilder(expression)
                .function(maxFunction) // Registering the custom max function
                .build();
        double result = exp.evaluate(); // Evaluate the expression

        return (long) result; // Return the result as a long
    }
    
}
