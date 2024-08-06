/*
 *  Data structure for a forum topic
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.forum.structures;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import online.nostrium.utils.TextFunctions;
import org.apache.commons.io.FileUtils;
import static org.apache.commons.lang3.StringUtils.isEmpty;


/**
 * Date: 2023-02-09
 * Place: Germany
 * @author brito
 */
public class ForumTopic {

    String id,
           title,
           content;
    
    boolean 
        isPinned = false,
        isFirstTopic = false,
        isLocked = false;
    
    @SuppressWarnings("unchecked")
    ArrayList<String> tags = new ArrayList();
    @SuppressWarnings("unchecked")
    ArrayList<ForumNote> forumNotes = new ArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIsPinned(boolean isPinned) {
        this.isPinned = isPinned;
    }

    public void setIsFirstTopic(boolean isFirstTopic) {
        this.isFirstTopic = isFirstTopic;
    }

    public String getContent() {
        return content;
    }

    public boolean isIsPinned() {
        return isPinned;
    }

    public boolean isIsFirstTopic() {
        return isFirstTopic;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public ArrayList<ForumNote> getForumNotes() {
        return forumNotes;
    }

    public boolean isIsLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
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
    public static ForumTopic jsonImport(File file) {
        if (file.exists() == false
                || file.isDirectory()
                || file.length() == 0) {
            return null;
        }
        try {
            String text = FileUtils.readFileToString(file, "UTF-8");
            Gson gson = new Gson();
            ForumTopic data = gson.fromJson(text, ForumTopic.class);
            return data;
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Produce a name compatible with files
     * @return 
     */
    public String getFilename() {
        if(isEmpty(title)){
            return id;
        }
        return id + "-" + TextFunctions.cleanString(title);
    }

    public void addNote(ForumNote note) {
        this.getForumNotes().add(note);
    }

}
