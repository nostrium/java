/*
 * The chat room
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.chat;

import com.google.gson.annotations.Expose;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import online.nostrium.main.Folder;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalColor;
import online.nostrium.user.User;
import online.nostrium.utils.FileFunctions;
import online.nostrium.utils.JsonTextFile;
import online.nostrium.utils.TextFunctions;
import static online.nostrium.utils.TextFunctions.addIfNew;

/**
 * @author Brito
 * @date: 2024-08-06
 * @location: Germany
 */
public class ChatRoom extends JsonTextFile {

    @Expose
    final String npub;

    @Expose
    String nsec;           // maybe used in the future for signing events

    @Expose
    ChatType chatType = ChatType.NORMAL;    // type of room

    @Expose
    String name, // one name without spaces
            description, // one-line description
            intro, // set initial screen when entering the room
            passwordHash, // define a password to enter (optional)
            registeredTime;  // when it was registered

    @Expose
    boolean showHistory = true,
            needPasswordToEnter = false,
            needApprovalToEnter = false;

    @Expose
    String roomParent;     // either null or the parent inside this chat

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
    private File folder;

    public ChatRoom(String npub) {
        this.npub = npub;
        folder = FileFunctions.getThirdLevelFolderWithNpubOnEnd
            (Folder.getFolderChat(), npub, true);
    }

    public void setFolder(File folder) {
        this.folder = folder;
    }
    
    public File getFolder(){
        return this.folder;
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
    public void delete() {
        FileFunctions.deleteFolderAndParentsIfEmpty(getFile(), Folder.getFolderChat());
    }

    @Override
    public File getFile() {
        File folderToBeUsed = getFolder();
        String filename = Folder.nameFolderChatRoom; // room.json
        File file = new File(folderToBeUsed, filename);
        return file;
    }

    public void addAdmin(String npub) {
        addIfNew(npub, this.admins);
    }

    /**
     * Gets the message box for the current day
     *
     * @return
     */
    public synchronized ChatArchive getMessagesToday() {
        // user can add the text here
        File file = ChatUtils.getFileMessageBoxForToday(folder);
        ChatArchive archive;

        if (file.exists() == false) {
            archive = new ChatArchive();
            archive.save(file);
        } else {
            archive = ChatArchive.jsonImport(file);
        }
        return archive;
    }

    /**
     * Gets the ChatArchives for each day in the past up to the specified number
     * of days.
     *
     * @param days the number of days in the past
     * @return an ArrayList of ChatArchive objects
     */
    public synchronized ArrayList<ChatArchive> getMessageArchivesForDay(int days) {
        ArrayList<ChatArchive> archiveList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        for (int i = 0; i <= days; i++) {
            LocalDate targetDate = LocalDate.now().minusDays(i);
            String formattedDate = targetDate.format(formatter);
            String filename = "messages_" + formattedDate + ".json";
            File file = new File(folder, filename);

            if (file.exists() && file.isFile()) {
                ChatArchive archive = ChatArchive.jsonImport(file);
                if (archive != null) {
                    archiveList.add(archive);
                }
            }
        }

        return archiveList;
    }

    /**
     * Gets the ChatMessages for each day in the past up to the specified number
     * of days.
     *
     * @param days the number of days in the past
     * @return an ArrayList of ChatMessage objects
     */
    public synchronized ArrayList<ChatMessage> getMessagesForDay(int days) {
        ArrayList<ChatMessage> messageList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        for (int i = 0; i <= days; i++) {
            LocalDate targetDate = LocalDate.now().minusDays(i);
            String formattedDate = targetDate.format(formatter);
            String filename = "messages_" + formattedDate + ".json";
            File file = new File(folder, filename);

            if (file.exists() && file.isFile()) {
                ChatArchive archive = ChatArchive.jsonImport(file);
                if (archive != null) {
                    ArrayList<ChatMessage> messages = archive.getMessages();
                    messageList.addAll(messages);
                }
            }
        }

        return messageList;
    }

    /**
     * Gets the ChatArchives between two specified dates within a 100-day
     * interval.
     *
     * @param startDateString the start date in YYYY-MM-DD format
     * @param endDateString the end date in YYYY-MM-DD format
     * @return an ArrayList of ChatArchive objects
     * @throws IllegalArgumentException if the interval between dates exceeds
     * 100 days
     */
    public synchronized ArrayList<ChatArchive>
            getMessagesBetweenDates(String startDateString, String endDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate startDate = LocalDate.parse(startDateString, formatter);
        LocalDate endDate = LocalDate.parse(endDateString, formatter);

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }

        if (startDate.plusDays(100).isBefore(endDate)) {
            throw new IllegalArgumentException("Date interval cannot exceed 100 days.");
        }

        ArrayList<ChatArchive> archiveList = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            String formattedDate = currentDate.format(formatter);
            String filename = "messages_" + formattedDate + ".json";
            File file = new File(folder, filename);
            ChatArchive archive;

            if (file.exists()) {
                archive = ChatArchive.jsonImport(file);
                archiveList.add(archive);
            }

            currentDate = currentDate.plusDays(1);
        }

        return archiveList;
    }

    /**
     * Retrieves the last XXXX messages, starting from today and going back each
     * day.
     *
     * @param numberOfMessages the number of messages to retrieve (1 to 999)
     * @return an ArrayList of ChatMessage objects, or null if the number of
     * messages is out of range
     */
    public ArrayList<ChatMessage> getLastMessages(int numberOfMessages) {
        if (numberOfMessages < 1 || numberOfMessages > 999) {
            return null;
        }

        ArrayList<ChatMessage> messageList = new ArrayList<>(numberOfMessages);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        int daysBack = 0;

        while (messageList.size() < numberOfMessages && daysBack <= 100) {
            LocalDate targetDate = LocalDate.now().minusDays(daysBack++);
            String formattedDate = targetDate.format(formatter);
            File file = new File(folder, "messages_" + formattedDate + ".json");

            if (!file.exists() || !file.isFile()) {
                continue;
            }

            ChatArchive archive = ChatArchive.jsonImport(file);
            if (archive == null) {
                continue;
            }

            ArrayList<ChatMessage> messages = archive.getMessages();
            int remainingMessages = numberOfMessages - messageList.size();

            // Add messages in reverse order to preserve the original chronology
            int start = Math.max(messages.size() - remainingMessages, 0);
            messageList.addAll(0, messages.subList(start, messages.size()));

            if (messageList.size() >= numberOfMessages) {
                break;
            }
        }

        return messageList;
    }

    /**
     * Add a new text on the chat box
     *
     * @param user
     * @param text
     * @return
     */
    public CommandResponse sendChatText(User user, String text) {
        if (userCannotAddText(user)) {
            return new CommandResponse(TerminalCode.DENIED);
        }

        // clean and normalize the text to remove weird characters
        text = TextFunctions.sanitizeChatMessage(text);
        if(TextFunctions.isValidText(text) == false){
            return new CommandResponse(TerminalCode.DENIED);
        }
        
        // get the local archive
        ChatArchive archive = this.getMessagesToday();
        if (archive == null) {
            return new CommandResponse(TerminalCode.FAIL);
        }

        // write the text message to disk
        ChatMessage message = new ChatMessage(user, text);
        archive.addMessage(message);
        File file = ChatUtils.getFileMessageBoxForToday(folder);
        archive.save(file);
        
        // all good
        return new CommandResponse(TerminalCode.OK, text);
    }
    

    /**
     * Check if a user is permitted to write on this chat
     *
     * @param user
     * @return
     */
    private boolean userCannotAddText(User user) {
        // is the user blacklisted?
        if (usersBlacklisted.contains(user.getNpub())) {
            return true;
        }

        // not a member at a password-closed group
        if (needPasswordToEnter
                && usersPermitted.contains(user.getNpub()) == false) {
            return true;
        }

        return false;
    }

}
