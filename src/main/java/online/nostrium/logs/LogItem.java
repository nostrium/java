/*
 * Log item
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.logs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.IOException;
import online.nostrium.servers.terminal.TerminalCode;
import org.apache.commons.io.FileUtils;

/**
 * @author Brito
 * @date: 2024-08-31
 * @location: Germany
 */
public class LogItem {
    
    @Expose
    final long timestamp;
    
    @Expose
    final String id;
    
    @Expose
    final TerminalCode code;
    
    @Expose
    final String text;
    
    @Expose
    final String data;

    public LogItem(long timestamp, String id, TerminalCode code, String text, String data) {
        this.timestamp = timestamp;
        this.id = id;
        this.code = code;
        this.text = text;
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getId() {
        if(id == null){
            return "[NONE]";
        }
        return id;
    }

    public TerminalCode getCode() {
        return code;
    }

    public String getText() {
        if(text == null){
            return "[NONE]";
        }
        return text;
    }

    public String getData() {
        if(data == null){
            return "[NONE]";
        }
        return data;
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
                //.setPrettyPrinting()
                ;
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }
    
    /**
     * Import a JSON into an object
     *
     * @param file
     * @return null if something went wrong
     */
    public static LogItem jsonImport(File file) {
        if (file.exists() == false
                || file.isDirectory()
                || file.length() == 0) {
            return null;
        }
        try {
            String text = FileUtils.readFileToString(file, "UTF-8");
            Gson gson = new Gson();
            LogItem item = gson.fromJson(text, LogItem.class);
            return item;
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
