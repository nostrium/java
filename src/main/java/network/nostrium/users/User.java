/*
 *  User detail
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package network.nostrium.users;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 * Date: 2024-08-04
 * Place: Germany
 * @author brito
 */
public class User {
    
    UserType userType = UserType.ANON;
    String npub = null;
    String nsec = null;
    String username = null;
    String displayName = null;
    String website = null;
    String aboutMe = null;
    String nostrVerifiedAddress = null;
    String registered = null;
    String lastLogin = null;
    
    
    /**
     * Save this user on the folder
     */
    public void save(){
        
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
    public static User jsonImport(File file) {
        if (file.exists() == false
                || file.isDirectory()
                || file.length() == 0) {
            return null;
        }
        try {
            String text = FileUtils.readFileToString(file, "UTF-8");
            Gson gson = new Gson();
            User data = gson.fromJson(text, User.class);
            return data;
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getNpub() {
        return npub;
    }

    public void setNpub(String npub) {
        this.npub = npub;
    }

    public String getNsec() {
        return nsec;
    }

    public void setNsec(String nsec) {
        this.nsec = nsec;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getNostrVerifiedAddress() {
        return nostrVerifiedAddress;
    }

    public void setNostrVerifiedAddress(String nostrVerifiedAddress) {
        this.nostrVerifiedAddress = nostrVerifiedAddress;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    
    
}
