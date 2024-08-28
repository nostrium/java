/*
 * The app for chatting with other users
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.chat;

import java.util.ArrayList;
import online.nostrium.main.Folder;
import online.nostrium.main.core;
import online.nostrium.notifications.NotificationType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalColor;
import online.nostrium.servers.terminal.screens.Screen;
import online.nostrium.apps.user.User;
import online.nostrium.apps.user.UserUtils;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.servers.terminal.TerminalUtils;
import online.nostrium.utils.TextFunctions;

/**
 * @author Brito
 * @date: 2024-08-06
 * @location: Germany
 */
public class TerminalChat extends TerminalApp {

    // load the room, but not the chat history
    public ChatRoom roomNow = null;
            
    public TerminalChat(Screen screenAssigned, User user) {
        super(screenAssigned, user);
        // make sure the room is not empty
        roomNow = ChatUtils.getOrCreateRoom(
                    Folder.nameRootChat, UserUtils.getUserAdmin()
            );
        // let's overwrite the previous LS command
        removeCommand("ls");
        addCommand(new CommandChatLs(this, roomNow));
        addCommand(new CommandChatClear(this));
    }

    @Override
    public String getDescription() {
        return "Chat with others";
    }

    @Override
    public String getIntro() {

        String title = "Chat";
        String intro = screen.getWindowFrame(core.config.colorAppsDefault, title);
        
        // read the number of messages
        int countMessages;
        ArrayList<ChatMessage> messages = roomNow.getMessagesForDay(5);
        countMessages = messages.size();
        if (countMessages > 0) {
            intro += "\n"
                    + "Messages this week: " + countMessages;
        }

        return intro;
    }

    @Override
    public String getName() {
        return "chat";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        
        // delete the current line before writing new stuff
        screen.deleteCurrentLine();
        
        // write the chat message on the room
        CommandResponse reply = roomNow.sendChatText(user, commandInput);
        // when something went wrong, stop it here
        if(reply.getCode() != TerminalCode.OK){
            return reply;
        }
        
        
        String line = createMessageLine(
                System.currentTimeMillis() / 1000L, 
                user.getDisplayName(), 
                reply.getText()
        );
        
        // write the new line
        screen.writeln(line);
        
        // send a notification
        core.sessions.sendNotification(
                this.getId(), 
                user, 
                NotificationType.UPDATE, 
                line
        );
        
        return new CommandResponse(TerminalCode.OK, "");
    }
    
    
    /**
     * The message that appears on the chat box to the user
     * @param timeCreated
     * @param userId
     * @param content
     * @return 
     */
    public String createMessageLine(long timeCreated, String userId, String content){
        String timestamp = TextFunctions.convertLongToDateTime(timeCreated);
        timestamp = screen.paint(TerminalColor.BLUE, timestamp);
        String line = timestamp
                    + " "
                    + "["
                    + userId
                    + "]"
                    + " "
                    + content
                    ;
        return line;
    }

    @Override
    public void receiveNotification(User userSender, NotificationType notificationType, Object object) {
        if(notificationType != NotificationType.UPDATE){
            return;
        }
        if(userSender.sameAs(this.user)){
            return;
        }
        String line = (String) object;
        screen.deleteCurrentLine();
        
        // break the text in two parts
        int i = line.indexOf("]") + 1;
        String line1 = line.substring(0, i);
        String line2 = line.substring(i);
        
        // write the text in different speed
        screen.write(line1);
        screen.writeLikeHuman(line2, 25);
        
        screen.writeUserPrompt(this);
    }
    
    @Override
    public String getId() {
        String path = TerminalUtils.getPath(this)
                + "#"
                + roomNow.name;
        return path;
    }

}
