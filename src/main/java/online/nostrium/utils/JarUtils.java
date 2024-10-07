/*
 * Utils related to Jar files
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.utils;

import java.util.Properties;
import java.util.jar.Manifest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Brito
 * @date: 2024-08-30
 * @location: Germany
 */
public class JarUtils {

   public static String getBuildTime() {
        try {
            String className = JarUtils.class.getSimpleName() + ".class";
            String classPath = JarUtils.class.getResource(className).toString();

            if (classPath.startsWith("jar")) {
                String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF";
                try (@SuppressWarnings("deprecation")
                    InputStream inputStream = new java.net.URL(manifestPath).openStream()) {
                    Manifest manifest = new Manifest(inputStream);
                    return manifest.getMainAttributes().getValue("Build-Time");
                }
            } else {
                return "Build time not found (not running from a JAR)";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading build time";
        }
    }
   
   public static String getVersion() {
        Properties properties = new Properties();
        try {
            // Dynamically determine the package name (groupId) and artifactId from the class package
            String packageName = JarUtils.class.getPackage().getName();
            String[] packageParts = packageName.split("\\.");
            String groupId = String.join("/", packageParts);
            String artifactId = packageParts[packageParts.length - 1]; // Assuming the last part of the package name is the artifactId

            // Construct the path to pom.properties dynamically
            String path = String.format("/META-INF/maven/%s/%s/pom.properties", groupId, artifactId);

            try (InputStream inputStream = JarUtils.class.getResourceAsStream(path)) {
                if (inputStream != null) {
                    properties.load(inputStream);
                    return properties.getProperty("version");
                } else {
                    return "Version not found";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading version";
        }
    }
   
    /**
     * Extracts the folder from inside the JAR to the specified output location.
     * It will only extract files that don't already exist on the disk.
     *
     * @param jarFolderPath The folder path inside the JAR (e.g., "resources/extract/")
     * @param outputFolder  The destination folder on the disk as a File object
     */
    public static void extractFolderFromJar(String jarFolderPath, File outputFolder) {
        try {
            // Get the URL of the resource folder (inside the JAR)
            URL resourceUrl = JarUtils.class.getClassLoader().getResource(jarFolderPath);
            if (resourceUrl == null) {
                System.out.println("Resource folder not found in the JAR");
                return;
            }

            // Convert the URL to the path of the JAR file
            String jarPath = resourceUrl.toURI().getSchemeSpecificPart();
            jarPath = jarPath.substring(5, jarPath.indexOf("!")); // Remove "file:" and everything after "!"

            // Open the JAR file
            try (JarFile jarFile = new JarFile(jarPath)) {
                Enumeration<JarEntry> entries = jarFile.entries();

                // Iterate through the entries in the JAR file
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();

                    // Check if the entry is in the target folder
                    if (entryName.startsWith(jarFolderPath) && !entry.isDirectory()) {
                        // Compute the output file path
                        String relativePath = entryName.substring(jarFolderPath.length());
                        File outputFile = new File(outputFolder, relativePath);

                        // Check if the file already exists on disk
                        if (!outputFile.exists()) {
                            // Ensure that the output directories exist
                            outputFile.getParentFile().mkdirs();

                            // Extract the file from the JAR
                            try (InputStream inputStream = jarFile.getInputStream(entry);
                                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {

                                byte[] buffer = new byte[1024];
                                int bytesRead;
                                while ((bytesRead = inputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, bytesRead);
                                }
                                System.out.println("Extracted: " + outputFile.getPath());
                            }
                        } else {
                            System.out.println("File already exists, skipping: " + outputFile.getPath());
                        }
                    }
                }
            }

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        
        
    }

    /**
     * Checks if the application is running from inside a JAR file.
     * 
     * @return true if running from a JAR (production mode), false if running from classes (development mode)
     */
    public static boolean isRunningFromJar() {
        // Get the path of the current class
        String classPath = JarUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        // Check if the class path ends with .jar (indicating that it's running from a JAR file)
        return classPath.endsWith(".jar");
    }
    

    public static void main(String[] args) {
        String buildTime = getBuildTime();
        System.out.println("Build Time: " + buildTime);
        String version = getVersion();
        System.out.println("Version: " + version);
    }
    
}
