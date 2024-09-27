/*
 * Data associated with an app
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.terminal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.folder.FolderUtils;
import org.apache.commons.io.FileUtils;

/**
 * @author Brito
 * @date: 2024-08-25
 * @location: Germany
 */
public final class AppData {

    final TerminalApp app;

    @Expose
    HashMap<String, Object> data = new HashMap<>();

    public AppData(TerminalApp app) {
        this.app = app;
        // try to load previous values
        load(app);
    }

    public void load(TerminalApp app) {
        File file = getFile();
        if (file == null || file.exists() == false) {
            return;
        }
        AppData fromDisk = AppData.jsonImport(file);
        if (fromDisk == null) {
            return;
        }
        data = fromDisk.getData();
    }

    public File getFile() {
        try {
            if (app == null || app.getFolder() == null) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        File folder = app.getFolder();
        String filename = FolderUtils.nameFileData;
        return new File(folder, filename);
    }

    public void put(String tag, Object object) {
        data.put(tag, object);
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void save() {
        try {
            String text = this.jsonExport();
            File file = this.getFile();
            FileUtils.writeStringToFile(file, text, "UTF-8");
        } catch (IOException ex) {
            Logger.getLogger(AppData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Export this object as JSON
     *
     * @return null if something went wrong
     */
    public String jsonExport() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                //.enableComplexMapKeySerialization()
                //.setLenient()
                .setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }

    /**
     * Import a JSON into an object
     *
     * @param file
     * @return null if something went wrong
     */
    public static AppData jsonImport(File file) {
        if (file.exists() == false
                || file.isDirectory()
                || file.length() == 0) {
            return null;
        }
        try {
            String text = FileUtils.readFileToString(file, "UTF-8");
            Gson gson = new Gson();
            AppData item = gson.fromJson(text, AppData.class);
            return item;
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean has(String tag) {
        return data.containsKey(tag);
    }

    public Object get(String tag) {
        return data.get(tag);
    }

    public void delete() {
        data = new HashMap<>();
        File file = this.getFile();
        if (file.exists()) {
            file.delete();
        }
    }

}
