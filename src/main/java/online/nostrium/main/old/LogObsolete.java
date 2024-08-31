/*
 *  Log messages to the console or files
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.main.old;

/**
 * Date: 2023-02-07
 * Place: Germany
 * @author brito
 */
public class LogObsolete {

    public static void write(String message){
        System.out.println(message);
    }

    public static void write(String message, String value){
        System.out.println(message + ": " + value);
    }
    
}
