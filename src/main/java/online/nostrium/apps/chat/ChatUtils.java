/*
 * Internal utils
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.chat;

import online.nostrium.user.UserUtils;
import online.nostrium.user.User;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import online.nostrium.folder.FolderUtils;
import static online.nostrium.nostr.NostrUtils.generateNostrKeys;
import online.nostrium.utils.FileFunctions;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.utils.TextFunctions;

/**
 * @author Brito
 * @date: 2024-08-06
 * @location: Germany
 */
public class ChatUtils {

    /**
     * Create the main chat room on the root of the app
     *
     * @param app
     * @return
     */
    public static ChatRoom createChatRoomMain(TerminalApp app) {
        User user = UserUtils.getUserAdmin();
        ChatRoom room = ChatUtils.getOrCreateRoom(app, FolderUtils.nameRootChat, user);
        return room;
    }

    /**
     * Create an chat room without saving it to disk
     *
     * @param user
     * @return a fresh chat room without saving to disk
     */
    public static ChatRoom createChatRoom(User user) {
        // generate random nostr keys
        String[] keys = generateNostrKeys();
        ChatRoom chatRoom = new ChatRoom(keys[1]);
        chatRoom.setNsec(keys[0]);

        // add the user as founder
        chatRoom.addAdmin(user.getNpub());

        // user needs to be saved to disk
        user.save();

        // set the name
        chatRoom.setName("randochat#"
                + chatRoom.getNpub().substring(0, 4)
        );

        // set the registration time
        String timestamp = TextFunctions.getDate();
        chatRoom.setRegisteredTime(timestamp);

        // all done
        return chatRoom;
    }

    /**
     * Gets the message box for the current day
     *
     * @param folder
     * @return
     */
    public static File getFileMessageBoxForToday(File folder) {
        LocalDate currentDate = LocalDate.now();
        String today = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String filename = "messages_" + today + ".json";
        File file = new File(folder, filename);
        return file;
    }
    
    /**
     * Gets the message box files for the specified number of days in the past.
     *
     * @param folder the folder containing the files
     * @param days   the number of days in the past
     * @return an ArrayList of File objects
     */
    public static ArrayList<File> getFileMessageBoxForPastDays(File folder, int days) {
        ArrayList<File> fileList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

        for (int i = 0; i <= days; i++) {
            LocalDate targetDate = currentDate.minusDays(i);
            String formattedDate = targetDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            String filename = "messages_" + formattedDate + ".json";
            File file = new File(folder, filename);

            if (file.exists() && file.isFile() && file.length() > 0) {
                fileList.add(file);
            }
        }

        return fileList;
    }
    
     /**
     * Gets the message box files between two specified dates.
     *
     * @param folder   the folder containing the files
     * @param startDateString the start date in YYYY-MM-DD format
     * @param endDateString   the end date in YYYY-MM-DD format
     * @return an ArrayList of File objects
     */
    public static ArrayList<File> getFileMessageBoxBetweenDates
            (File folder, String startDateString, String endDateString) {
        ArrayList<File> fileList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        LocalDate startDate = LocalDate.parse(startDateString, formatter);
        LocalDate endDate = LocalDate.parse(endDateString, formatter);

        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            String formattedDate = currentDate.format(formatter);
            String filename = "messages_" + formattedDate + ".json";
            File file = new File(folder, filename);

            if (file.exists() && file.isFile()) {
                fileList.add(file);
            }

            currentDate = currentDate.plusDays(1);
        }

        return fileList;
    }



    /**
     * Look at all available chat rooms, provide the first one that is matching
     * the name that was provided
     *
     * @param name name without spaces of the room
     * @param user to be named as owner when needed to create it
     * @return
     */
    public static ChatRoom getOrCreateRoom(TerminalApp app, String name, User user) {
        File folderBase = FolderUtils.getFolderChat();
        ArrayList<File> files
                = FileFunctions.searchFiles(folderBase, FolderUtils.nameFolderChatRoom
                );
        // no default room found? Create a new one
        if (files.isEmpty()) {
            ChatRoom room = new ChatRoom(user.getNpub());
            room.setName(name);
            room.save();
            return room;
        }

        // iterate each chat room and find the first with the right name
        for (File file : files) {
            // try to load the room
            ChatRoom room = ChatRoom.jsonImport(file, ChatRoom.class);
            if (room == null) {
                app.log(TerminalCode.INVALID, "Invalid room file", file.getPath());
                continue;
            }
            // check the name, got a match? return it
            if(room.getName().equalsIgnoreCase(name)){
                room.setFolder(file.getParentFile());
                return room;
            }
        }

        // not found? create a new one
        ChatRoom room = new ChatRoom(user.getNpub());
        room.setName(name);
        room.save();
        return room;
    }
    
     /**
     * Retrieves all available and valid chat rooms.
     *
     * @return An ArrayList of valid ChatRoom objects.
     */
    public static ArrayList<ChatRoom> getChatRooms(TerminalApp app) {
        ArrayList<ChatRoom> chatRooms = new ArrayList<>();
        File folderBase = FolderUtils.getFolderChat();
        ArrayList<File> files = FileFunctions.searchFiles(folderBase, FolderUtils.nameFolderChatRoom);

        for (File file : files) {
            // Attempt to import the chat room from the file
            ChatRoom room = ChatRoom.jsonImport(file, ChatRoom.class);
            if (room != null) {
                // Room is valid, add to the list
                room.setFolder(file.getParentFile());
                chatRooms.add(room);
            } else {
                // Room is invalid, log the issue
                app.log(TerminalCode.INVALID, "Invalid room file", file.getPath());
            }
        }

        return chatRooms;
    }

}
