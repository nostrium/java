/*
 *  Functions related to files
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Date: 2023-02-10
 * Place: Germany
 *
 * @author brito
 */
public class FileFunctions {

    /**
     * Looks inside a folder for a file that begins and ends with specific
     * texts.
     *
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
                    && file.getName().endsWith(endString)) {
                return file;
            } else if (file.isDirectory()) {
                searchForFile(file, startString, endString);
            }
        }
        return null;
    }

    /**
     * Provides the folder based on texts such as nsec, creates the folders when
     * they don't exist.
     *
     * @param startingFolder
     * @param text
     * @return
     */
    public static File getThirdLevelFolder(
            File startingFolder, String text, boolean createFolders) {
        if (text == null || text.length() < 9) {
            return null;
        }

        try {
            String firstThree = text.substring(0, 3);
            String secondThree = text.substring(3, 6);
            String thirdThree = text.substring(6, 9);

            File firstLevelFolder = new File(startingFolder, firstThree);
            File secondLevelFolder = new File(firstLevelFolder, secondThree);
            File thirdLevelFolder = new File(secondLevelFolder, thirdThree);

            if (createFolders && !thirdLevelFolder.exists()) {
                boolean created = thirdLevelFolder.mkdirs();
                if (!created) {
                    return null;
                }
            }

            return thirdLevelFolder;
        } catch (Exception e) {
            return null;
        }
    }
    
     
    public static boolean saveStringToFile(File file, String content) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
