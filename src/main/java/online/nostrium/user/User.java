/*
 *  User detail
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.IOException;
import online.nostrium.main.Folder;
import online.nostrium.main.core;
import online.nostrium.user.alternative.AccountsManager;
import online.nostrium.utils.EncryptionUtils;
import online.nostrium.utils.FileFunctions;
import org.apache.commons.io.FileUtils;

/**
 * Date: 2024-08-04
 * Place: Germany
 *
 * @author brito
 */
public class User {

    
    // fields written to disk in clear text
    @Expose
    String npub = null,
            nsecEncrypted = null,
            username = null,
            displayName = null,
            website = null,
            aboutMe = null,
            nostrVerifiedAddress = null,
            registeredTime = null,
            lastLoginTime = null,
            passwordHash = null;

    @Expose
    UserType userType = UserType.ANON;
    
    // fields that are not exposed (just stored in memory)
    String password = null,
            nsec = null;

    
    AccountsManager alternativeAccounts = new AccountsManager(this);
    
    /**
     * Provides the JSON where the file should be created
     * It does not create folders
     * @return 
     */
    public File getFile(){
        File folder = FileFunctions.getThirdLevelFolderForUser(
                Folder.getFolderUsers(), npub, false);
        File file = new File(folder, this.npub + Folder.nameEndingJsonUser);
        return file;
    }
    
    public void delete(){
        FileFunctions.deleteFolderAndParentsIfEmpty
            (getFile(), Folder.getFolderUsers());
    }
    
    /**
     * Save this user on the folder
     * @return 
     */
    public boolean save() {
        File file = getFile();
        File folder = file.getParentFile();
          
        if(folder != null && folder.exists() == false){
            folder.mkdirs();
        }
        
        if(folder == null || folder.exists() == false){
            return false;
        }
        
        String text = this.jsonExport();
        
        // save it do disk
        boolean result = FileFunctions.saveStringToFile(file, text);
        
        if(result == false){
            return false;
        }
        
        if(file.exists() == false){
            return false;
        }

        // all done
        return true;
    }

    public AccountsManager getAlternativeAccounts() {
        return alternativeAccounts;
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
        if(password != null){
            // encrypt the nsec
            this.nsecEncrypted = EncryptionUtils.encrypt(nsec, password);
            this.save();
        }
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

    public String getRegisteredTime() {
        return registeredTime;
    }

    public void setRegisteredTime(String registered) {
        this.registeredTime = registered;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLogin) {
        this.lastLoginTime = lastLogin;
    }


    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    
    public boolean hasPassword() {
        return getPasswordHash() != null;
    }


    public String getNsecEncrypted() {
        return nsecEncrypted;
    }

    public void setNsecEncrypted(String nsecEncrypted) {
        this.nsecEncrypted = nsecEncrypted;
    }

    public boolean sameAs(User userTarget) {
        return this.npub.equalsIgnoreCase(userTarget.npub);
    }

    public File getFolder(boolean createFolder) {
        File folder = FileFunctions.getThirdLevelFolderForUser(
                Folder.getFolderUsers(), npub, false);
        
        if(createFolder){
            folder.mkdirs();
        }
        
        return folder;
    }

    /**
     * This value is never written to disk
     * @return 
     */
    public String getPassword() {
        return password;
    }

    /**
     * This value is never written to disk
     * @param password 
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        String name = this.username;
        if(username == null){
            // use the npub
            name = this.npub;
        }
        return name + "@" + core.config.getDomain();
    }

    /**
     * Verifies if a user has a user name
     * @return 
     */
    public boolean hasUsername() {
        return username != null;
    }
    
    

}
