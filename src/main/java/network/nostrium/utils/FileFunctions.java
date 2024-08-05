/*
 *  Functions related to files
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package network.nostrium.utils;

import java.io.File;

/**
 * Date: 2023-02-10
 * Place: Germany
 * @author brito
 */
public class FileFunctions {

    /**
     * Looks inside a folder for a file that
     * begins and ends with specific texts.
     * @param folder
     * @param startString
     * @param endString
     * @return 
     */
    public static File searchForFile(File folder, String startString, String endString) {
        File[] listOfFiles = folder.listFiles();
        
        for (File file : listOfFiles) {
            if (file.isFile() 
                    && file.getName().startsWith(startString)
                    && file.getName().endsWith(endString)
                    ) {
                return file;
            } else if (file.isDirectory()) {
                searchForFile(file, startString, endString);
            }
        }
        return null;
    }
    
}
