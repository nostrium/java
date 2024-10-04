/*
 * Filters for restricted files and folders
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.session;

import java.io.File;

/**
 * @author Brito
 * @date: 2024-10-04
 * @location: Germany
 */
public class RestrictedFiles {

    /**
     * Files that cannot be shown to normal users
     * @param file
     * @return 
     */
    public static boolean dontList(File file){
        String filename = file.getName();
        return filename.endsWith("-user.json");
    }
    
}
