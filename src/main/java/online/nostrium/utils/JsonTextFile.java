/*
 *  Import and export JSON files based on objects
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class for importing and exporting objects as JSON files.
 *
 * This class provides methods to export an object to a JSON file and import it
 * back. The class that extends this abstract class should define the file where
 * the data will be saved.
 *
 * Date: 2023-02-08 Place: Germany
 *
 * @author brito
 */
public abstract class JsonTextFile {

    /**
     * Get the file where the data will be saved.
     *
     * @return the file where the data will be saved
     */
    public abstract File getFile();

    /**
     * Export this object as a JSON string.
     *
     * @return JSON string or null if something went wrong
     */
    public String jsonExport() {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .setPrettyPrinting();
            Gson gson = gsonBuilder.create();
            return gson.toJson(this);
        } catch (Exception e) {
            Logger.getLogger(JsonTextFile.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * Import a JSON into an object of the specified type.
     *
     * @param <T> the type of the object to be created
     * @param file the file containing JSON
     * @param clazz the class of the object to be created
     * @return object of type T or null if something went wrong
     */
    public static <T extends JsonTextFile> T jsonImport(File file, Class<T> clazz) {
        if (!file.exists() || file.isDirectory() || file.length() == 0) {
            return null;
        }
        try {
            @SuppressWarnings("deprecation")
            String text = FileUtils.readFileToString(file);
            Gson gson = GsonUtils.createGson(); // Use the custom Gson instance
            return gson.fromJson(text, clazz);
        } catch (JsonSyntaxException | IOException e) {
            Logger.getLogger(JsonTextFile.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * Save this object as JSON to its file.
     */
    public void save() {
        saveToFile(getFile());
    }

    /**
     * Save this object as JSON to the specified file.
     *
     * @param file the file where the JSON will be saved
     */
    @SuppressWarnings("deprecation")
    public void saveToFile(File file) {
        try {
            String data = jsonExport();
            if (data != null) {
                FileUtils.writeStringToFile(file, data);
                Logger.getLogger(JsonTextFile.class.getName()).log(Level.INFO, "Saved file: {0}", file.getPath());
            } else {
                Logger.getLogger(JsonTextFile.class.getName()).log(Level.SEVERE, "Failed to export JSON");
            }
        } catch (IOException ex) {
            Logger.getLogger(JsonTextFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Load the configuration from its default file. When it does not exist,
     * then it will create one.
     *
     * @param <T> the type of the object to be created
     * @param clazz the class of the object to be created
     * @return the loaded configuration object, or a new one if the file does
     * not exist or is invalid
     */
    public <T extends JsonTextFile> T loadConfig(Class<T> clazz) {
        return loadConfigFromFile(clazz, getFile());
    }

    /**
     * Load the configuration from the specified file. When it does not exist,
     * then it will create one.
     *
     * @param <T> the type of the object to be created
     * @param clazz the class of the object to be created
     * @param file the file from which to load the configuration
     * @return the loaded configuration object, or a new one if the file does
     * not exist or is invalid
     */
    public <T extends JsonTextFile> T loadConfigFromFile(Class<T> clazz, File file) {
        // If there is no file, create one with default values
        if (!file.exists()) {
            try {
                T config = clazz.getDeclaredConstructor().newInstance();
                config.saveToFile(file);
                return config;
            } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                Logger.getLogger(JsonTextFile.class.getName()).log(Level.SEVERE, null, e);
                return null;
            }
        }

        // The config file exists, let's load it
        T config = JsonTextFile.jsonImport(file, clazz);
        if (config == null) {
            Logger.getLogger(JsonTextFile.class.getName()).log(Level.WARNING, "Invalid config, using default values: {0}", file.getPath());
            try {
                config = clazz.getDeclaredConstructor().newInstance();
                config.saveToFile(file);
            } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                Logger.getLogger(JsonTextFile.class.getName()).log(Level.SEVERE, null, e);
                return null;
            }
        }
        // The config file is valid, use it
        return config;
    }
}
