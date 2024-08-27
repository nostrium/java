/*
 * Time-related functions
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.utils;

/**
 * @author Brito
 * @date: 2024-08-27
 * @location: Germany
 */
public class time {

    /**
     * Pauses the current thread for a while
     *
     * @param time_to_wait in seconds
     */
    public static void wait(int time_to_wait) {

        try {

            Thread.sleep(time_to_wait * 1000);
        } catch (InterruptedException ex) {
        }

    }

    /**
     * Pauses the current thread for a while
     *
     * @param time_to_wait in Milliseconds
     */
    public static void waitMs(long time_to_wait) {

        try {

            Thread.sleep(time_to_wait);
        } catch (InterruptedException ex) {
        }

    }
    
}
