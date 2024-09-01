/*
 * Template for a language
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.translation;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import online.nostrium.logs.Log;
import online.nostrium.main.Folder;
import online.nostrium.servers.terminal.TerminalCode;
import org.apache.commons.io.FileUtils;

/**
 * @author Brito
 * @date: 2024-09-01
 * @location: Germany
 */
public class Language {
    
    @Expose
    final String id;

    @Expose
    HashMap<String, String> list = new HashMap<>();

    public Language(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


    public String get(String key, String optionalNotFound) {
        if (!list.containsKey(key)) {
            // add a new key
            list.put(key, optionalNotFound);
            save();
        }
        return list.get(key);
    }

    public String get(String key) {
        if (!list.containsKey(key)) {
            // add a new key
            list.put(key, key);
            save();
        }
        return list.get(key);
    }

    /**
     * Export this object as JSON
     *
     * @return null if something went wrong
     */
    public String jsonExport() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                //.excludeFieldsWithoutExposeAnnotation()
                //.enableComplexMapKeySerialization()
                //.setLenient()
                //.serializeNulls()
                .setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        
        String json = gson.toJson(this);
        if (json == null) {
            Log.write(TerminalCode.CRASH, "JSON serialization failed for Language object");
        }
        return json;
    }

    private File getFile() {
        File folder = Folder.getFolderLang();
        String filename = "lang_" + getId() + ".json";
        File file = new File(folder, filename);
        return file;
    }

    /**
     * Import a JSON
     *
     */
    public void jsonImport() {
        File file = getFile();
        if (!file.exists() || file.isDirectory() || file.length() == 0) {
            return;
        }
        try {
            String text = FileUtils.readFileToString(file, "UTF-8");
            Gson gson = new GsonBuilder()
                    .create();
            Language item = gson.fromJson(text, getClass());
            this.list.clear();
            this.list.putAll(item.list);
        } catch (JsonSyntaxException | IOException e) {
            Log.write(Folder.nameFolderLang, TerminalCode.CRASH,
                    "Failed to read language file", file.getPath());
        }
    }

    public void save() {
        File file = getFile();
        String data = jsonExport();
        try {
            FileUtils.writeStringToFile(file, data, "UTF-8");
        } catch (IOException ex) {
            Log.write(Folder.nameFolderLang, TerminalCode.CRASH,
                    "Failed to write language file", file.getPath());
        }
    }

    public boolean hasText(String text) {
        return list.containsKey(text);
    }

    public void add(String key, String value) {
        list.put(key, value);
    }

}
