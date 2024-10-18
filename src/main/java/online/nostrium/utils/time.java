/*
 * Time-related functions
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author Brito
 * @date: 2024-08-27
 * @location: Germany
 */
public class time {

    public static String getTimeISO() {
        // Create a Date object
        Date date = new Date();
        // Create a SimpleDateFormat to format the date in ISO 8601 format
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        
        // Return the formatted date as a string
        return isoFormat.format(date);
    }
    
    public static String getCurrentDay() {
        // Create a Date object
        Date date = new Date();
        // Create a SimpleDateFormat to format the date in ISO 8601 format
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        // Return the formatted date as a string
        return isoFormat.format(date);
    }
    
    
    public static String getCurrentYear() {
        // Create a Date object
        Date date = new Date();
        // Create a SimpleDateFormat to format the date in ISO 8601 format
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy");
        
        // Return the formatted date as a string
        return isoFormat.format(date);
    }
    
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
    
    public static long getCurrentDayInUnix() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        
        // Convert the date to Unix time (seconds since 1970-01-01 00:00:00 UTC)
        long unixTime = currentDate
                            .atStartOfDay(ZoneId.systemDefault())
                            .toEpochSecond();
        
        return unixTime;
    }
    
    public static long getCurrentTimeInUnix() {
        // Get the current time as Unix timestamp
        long unixTime = Instant.now().getEpochSecond();
        
        return unixTime;
    }

    
}
