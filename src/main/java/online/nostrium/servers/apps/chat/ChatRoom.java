/*
 * The chat room
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.chat;

import com.google.gson.annotations.Expose;
import java.io.File;
import java.util.ArrayList;
import online.nostrium.main.Folder;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalColor;
import online.nostrium.users.User;
import online.nostrium.utils.FileFunctions;
import online.nostrium.utils.JsonTextFile;
import static online.nostrium.utils.TextFunctions.addIfNew;


/**
 * @author Brito
 * @date: 2024-08-06
 * @location: Germany
 */
public class ChatRoom extends JsonTextFile{
    
    @Expose
    final String npub;
    
    @Expose
    String
            nsec;           // maybe used in the future for signing events
    
    @Expose
    ChatType chatType = ChatType.NORMAL;    // type of room
    
    @Expose
    String name,            // one name without spaces
           description,     // one-line description
           intro,           // set initial screen when entering the room
           passwordHash,    // define a password to enter (optional)
           registeredTime;  // when it was registered

    
    @Expose
    boolean showHistory = true,
            needPasswordToEnter = false,
            needApprovalToEnter = false;
    
    @Expose
    String
            roomParent;     // either null or the parent inside this chat
    
    
    @Expose
    TerminalColor defaultTextColor = TerminalColor.WHITE;
    
    
    @SuppressWarnings("unchecked")
    @Expose
    ArrayList<String> subrooms = new ArrayList();
    
    
    @SuppressWarnings("unchecked")
    @Expose
    ArrayList<String> admins = new ArrayList();
    
    @SuppressWarnings("unchecked")
    @Expose
    ArrayList<String> mods = new ArrayList();     // the moderator team
    
    @SuppressWarnings("unchecked")
    @Expose
    ArrayList<String> tags = new ArrayList();     // tags defining this chat room
    
    @SuppressWarnings("unchecked")
    @Expose
    ArrayList<String> usersPermitted = new ArrayList();     // users permitted to talk
    
    @SuppressWarnings("unchecked")
    @Expose
    ArrayList<String> usersBlacklisted = new ArrayList();     // users permitted to talk
    
    // the folder where we keep the work files
    private File folder = null;
    
    public ChatRoom(String npub) {
        this.npub = npub;
    }
    
    
    public String getNsec() {
        return nsec;
    }

    public void setNsec(String nsec) {
        this.nsec = nsec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TerminalColor getDefaultTextColor() {
        return defaultTextColor;
    }

    public void setDefaultTextColor(TerminalColor defaultTextColor) {
        this.defaultTextColor = defaultTextColor;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public boolean isShowHistory() {
        return showHistory;
    }

    public void setShowHistory(boolean showHistory) {
        this.showHistory = showHistory;
    }

    public boolean isNeedPasswordToEnter() {
        return needPasswordToEnter;
    }

    public void setNeedPasswordToEnter(boolean needPasswordToEnter) {
        this.needPasswordToEnter = needPasswordToEnter;
    }

    public boolean isNeedApprovalToEnter() {
        return needApprovalToEnter;
    }

    public void setNeedApprovalToEnter(boolean needApprovalToEnter) {
        this.needApprovalToEnter = needApprovalToEnter;
    }

    public ArrayList<String> getSubrooms() {
        return subrooms;
    }

    public ArrayList<String> getAdmins() {
        return admins;
    }

    public ArrayList<String> getMods() {
        return mods;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public ArrayList<String> getUsersPermitted() {
        return usersPermitted;
    }

    public ArrayList<String> getUsersBlacklisted() {
        return usersBlacklisted;
    }

    public String getRoomParent() {
        return roomParent;
    }

    public void setRoomParent(String roomParent) {
        this.roomParent = roomParent;
    }

    public String getNpub() {
        return npub;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRegisteredTime() {
        return registeredTime;
    }

    public void setRegisteredTime(String registeredTime) {
        this.registeredTime = registeredTime;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }
    
    /**
     * Delete this folder and related files
     */
    public void delete(){
        FileFunctions.deleteFolderAndParentsIfEmpty
            (getFile(), Folder.getFolderChat());
    }

    public File getFolder(){
        if(folder != null){
            return folder;
        }
        File folderBase = Folder.getFolderChat();
        File folderMiddle = FileFunctions.getFirstLevelFolder(folderBase, npub, true);
        File folderCreated = new File(folderMiddle, npub);
        if(folderCreated.exists() == false){
            folderCreated.mkdirs();
        }
        folder = folderCreated;
        return folderCreated;
    }
    
    @Override
    public File getFile() {
        File folderToBeUsed = getFolder();
        String filename = "room.json";
        File file = new File(folderToBeUsed, filename);
        return file;
    }

    public void addAdmin(String npub) {
        addIfNew(npub, this.admins);
    }

    /**
     * Gets the message box for the current day
     * @return 
     */
    public ChatArchive getMessagesToday() {
        // user can add the text here
        File file = ChatUtils.getFileMessageBoxForToday(folder);
        ChatArchive archive;
        
        if(file.exists() == false){
            archive = new ChatArchive();
            archive.save(file);
        }else{
            archive = ChatArchive.jsonImport(file);
        }
        return archive;
    }
    
    
    /**
     * Add a new text on the chat box
     * @param user
     * @param text 
     * @return  
     */
    public CommandResponse addText(User user, String text) {
        if(userCannotAddText(user)){
            return new CommandResponse(TerminalCode.DENIED);
        }
        
        ChatArchive archive = this.getMessagesToday();
                
        if(archive == null){
            return new CommandResponse(TerminalCode.FAIL);
        }
        
        // add the text message
        ChatMessage message = new ChatMessage(user, text);
        archive.addMessage(message);
        File file = ChatUtils.getFileMessageBoxForToday(folder);
        archive.save(file);
        
        
        // all good
        return new CommandResponse(TerminalCode.OK);
    }

    /**
     * Check if a user is permitted to write on this chat
     * @param user
     * @return 
     */
    private boolean userCannotAddText(User user) {
        // is the user blacklisted?
        if(usersBlacklisted.contains(user.getNpub())){
            return true;
        }
        
        // not a member at a password-closed group
        if(needPasswordToEnter 
                && usersPermitted.contains(user.getNpub()) == false){
            return true;
        }
        
        return false;
    }


}
