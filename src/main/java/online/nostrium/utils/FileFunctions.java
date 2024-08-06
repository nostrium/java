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
     * @param startingFolder The starting folder path.
     * @param text The input text to determine folder structure.
     * @param createFolders Flag to indicate whether to create the folders if they don't exist.
     * @return The folder located at the specified level, or null if folder creation fails or text is invalid.
     */
    public static File getFirstLevelFolder(File startingFolder, String text, boolean createFolders) {
        if (text == null || text.length() < 3) {
            return null;
        }

        try {
            String firstChar = text.substring(0, 1);
            String secondChar = text.substring(1, 2);
            String thirdChar = text.substring(2, 3);

            File firstLevelFolder = new File(startingFolder, firstChar);
            File secondLevelFolder = new File(firstLevelFolder, secondChar);
            File thirdLevelFolder = new File(secondLevelFolder, thirdChar);

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
    
    /**
     * Deletes the folder where the JSON file is located and deletes empty parent folders
     * up to the base path.
     *
     * @param jsonFile The JSON file whose containing folder is to be deleted.
     * @param basePath The base path which should not be deleted.
     */
    public static void deleteFolderAndParentsIfEmpty(File jsonFile, File basePath) {
        if (jsonFile == null || basePath == null) {
            return;
        }

        // Delete all files and subfolders in the folder where the JSON file is located
        File folder = jsonFile.getParentFile();
        if (folder != null) {
            deleteRecursively(folder);
        }

        // Delete empty parent folders up to the base path
        File parentFolder = folder.getParentFile();
        while (parentFolder != null && !parentFolder.equals(basePath)) {
            if (parentFolder.list().length == 0) {
                parentFolder.delete();
                parentFolder = parentFolder.getParentFile();
            } else {
                break;
            }
        }
    }

    /**
     * Deletes the folder where the JSON file is located and deletes empty parent folders
     * up to the base path.
     *
     * @param jsonFile The JSON file whose containing folder is to be deleted.
     * @param basePath The base path which should not be deleted.
     */
    public static void deleteFolderIfEmpty(File jsonFile, File basePath) {
        if (jsonFile == null || basePath == null) {
            return;
        }
        
        // delete the file itself
        jsonFile.delete();

        File folder = jsonFile.getParentFile();
        while (folder != null && !folder.equals(basePath)) {
            if (folder.list().length == 0) {
                folder.delete();
                folder = folder.getParentFile();
            } else {
                break;
            }
        }
    }
    
    /**
     * Recursively deletes a directory and all its contents.
     *
     * @param file The directory to delete.
     */
    public static void deleteRecursively(File file) {
        if (file.isDirectory()) {
            File[] entries = file.listFiles();
            if (entries != null) {
                for (File entry : entries) {
                    deleteRecursively(entry);
                }
            }
        }
        file.delete();
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
