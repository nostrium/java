/*
 *  Functions related to numbers and math
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.utils;

import java.util.Random;

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
    
}
