/*
 * Basic utilities for the archive
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.archive;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Brito
 * @date: 2024-09-25
 * @location: Germany
 */
public class ArchiveUtils {

    /**
     * Creates a filename for the file that is suited to the archive type
     * @param title
     * @return null when the title is not valid
     */
     public static String generateFilename(String title) {
        // Current date in the format yyyy-MM-dd
        String timestamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        
        // Replace invalid characters (non-alphanumeric) with an underscore
        String sanitizedTitle = title.replaceAll("[^a-zA-Z0-9]", "_");

        // Remove any leading or trailing underscores
        sanitizedTitle = sanitizedTitle.replaceAll("^_|_$", "");

        // Replace consecutive underscores with a single underscore
        sanitizedTitle = sanitizedTitle.replaceAll("__+", "_");

        // Combine timestamp and sanitized title
        String filename = timestamp + "_" + sanitizedTitle;

        // Limit filename to 60 characters
        if (filename.length() > 60) {
            filename = filename.substring(0, 60);
        }

        return filename;
    }   


    
}
