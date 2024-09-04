/*
 * Output a directory tree in ASCII format
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.utils.ascii;

/**
 * @author Brito
 * @date: 2024-09-02
 * @location: Germany
 */
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Comparator;

public class DirectoryTreeBuilder {


    private static String indent = " ";  // Variable to control indentation

    public static void setIndentation(String indentation) {
        indent = indentation;
    }

    public static String buildTree(File folder) {
        StringBuilder tree = new StringBuilder();
        File[] subFolders = folder.listFiles(File::isDirectory);

        // Sort subfolders by creation date
        if (subFolders != null) {
            Arrays.sort(subFolders, Comparator.comparing(DirectoryTreeBuilder::getCreationTime));

            for (int i = 0; i < subFolders.length; i++) {
                String prefix = (i == subFolders.length - 1) ? "" : "|";
                tree.append(buildTree(subFolders[i], prefix, ""));
            }
        }
        return tree.toString();
    }

    private static String buildTree(File folder, String prefix, String indentation) {
        StringBuilder tree = new StringBuilder();
        int fileCount = countFilesRecursively(folder);
        tree.append(indentation).append("+-- ").append(folder.getName()).append(" (").append(fileCount).append(")\n");

        File[] subFolders = folder.listFiles(File::isDirectory);

        // Sort subfolders by creation date
        if (subFolders != null) {
            Arrays.sort(subFolders, Comparator.comparing(DirectoryTreeBuilder::getCreationTime));

            for (int i = 0; i < subFolders.length; i++) {
                String newIndentation = indentation + (prefix.equals("|") ? "|" + indent : indent + indent);
                String newPrefix = (i == subFolders.length - 1) ? "" : "|";
                tree.append(buildTree(subFolders[i], newPrefix, newIndentation));
            }
        }
        return tree.toString();
    }

    private static int countFilesRecursively(File folder) {
        int count = 0;
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    count++;
                } else if (file.isDirectory()) {
                    count += countFilesRecursively(file);
                }
            }
        }
        return count;
    }

    private static long getCreationTime(File file) {
        try {
            Path filePath = file.toPath();
            BasicFileAttributes attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
            return attributes.creationTime().toMillis();
        } catch (IOException e) {
            e.printStackTrace();
            return Long.MAX_VALUE; // If an error occurs, place the file last in sorting
        }
    }

    public static void main(String[] args) {
        File rootFolder = new File(".");  // Replace with your folder path
        String tree = buildTree(rootFolder);
        System.out.println(tree);
    }
}
