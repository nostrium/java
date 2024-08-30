/*
 * Utils related to Jar files
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.jar.Manifest;

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


    public static void main(String[] args) {
        String buildTime = getBuildTime();
        System.out.println("Build Time: " + buildTime);
        String version = getVersion();
        System.out.println("Version: " + version);
    }
    
}
