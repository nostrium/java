/*
 * Forum user
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.main.old.forum.structures;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import online.nostrium.utils.TextFunctions;
import org.apache.commons.io.FileUtils;

/**
 * Date: 2023-02-10
 * Place: Germany
 * @author brito
 */
public class ForumUser {

    String id;
    String title;
    String memberGroup;    // mods, devs, etc
    String memberLevel;    // silver, gold, etc
    String imageURL;
    String country;
    String location;
    String interests;
    String signatureText;
    
    @SuppressWarnings("unchecked")
    HashMap<String, String> customFields = new HashMap();
    
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
    public static ForumUser jsonImport(File file) {
        if (file.exists() == false
                || file.isDirectory()
                || file.length() == 0) {
            return null;
        }
        try {
            String text = FileUtils.readFileToString(file, "UTF-8");
            Gson gson = new Gson();
            ForumUser data = gson.fromJson(text, ForumUser.class);
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
//        if(isEmpty(title)){
//            return id + ".json";
//        }
//        return id + "-" + Text.cleanString(title) + ".json";
        String text = TextFunctions.cleanString(id);
        return text + ".json";
    }

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

    public String getMemberGroup() {
        return memberGroup;
    }

    public void setMemberGroup(String memberGroup) {
        this.memberGroup = memberGroup;
    }

    public void setMemberLevel(String memberLevel) {
        this.memberLevel = memberLevel;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getSignatureText() {
        return signatureText;
    }

    public void setSignatureText(String signatureText) {
        this.signatureText = signatureText;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public HashMap<String, String> getCustomFields() {
        return customFields;
    }
 
    
}
