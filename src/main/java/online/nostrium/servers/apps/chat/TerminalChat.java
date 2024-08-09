/*
 * The app for chatting with other users
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.chat;

import java.util.ArrayList;
import online.nostrium.main.Folder;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.Screen;
import online.nostrium.servers.terminal.TerminalApp;
import static online.nostrium.servers.terminal.TerminalColor.BLUE;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.users.User;
import online.nostrium.users.UserUtils;
import online.nostrium.utils.TextFunctions;

/**
 * @author Brito
 * @date: 2024-08-06
 * @location: Germany
 */
public class TerminalChat extends TerminalApp {

    // load the room, but not the chat history
    public ChatRoom roomNow
            = ChatUtils.getOrCreateRoom(
                    Folder.nameRootChat, UserUtils.getUserAdmin()
            );

    public TerminalChat(TerminalType terminalType, User user) {
        super(terminalType, user);
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
        String text = TextFunctions.getWindowFrame(title);

        String intro
                = paint(BLUE,
                        text
                );

        // read the number of messages
        int countMessages = 0;
//         ChatArchive messagesToday = this.roomNow.getMessagesToday();
//        int countMessages = messagesToday.getMessages().size();
//        if(countMessages > 0){
//            intro += "\n"
//                    + "Messages today: " + countMessages;
//        }
        //if(countMessages == 0){
        ArrayList<ChatMessage> messages = roomNow.getMessagesForDay(5);
        countMessages = messages.size();
        if (countMessages > 0) {
            intro += "\n"
                    + "Messages this week: " + countMessages;
        }

        // }
        return intro;
    }

    @Override
    public String getName() {
        return "chat";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        //ChatArchive archive = roomNow.getMessagesToday();
        
        String text = 
                Screen.ANSI_CLEAR_LINE 
                + Screen.ANSI_CURSOR_TO_LINE_START
                + commandInput;
        
        return roomNow.sendChatText(user, text);
    }

}
