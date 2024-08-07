/*
 * The app for chatting with other users
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.chat;

import online.nostrium.main.Folder;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
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

    ChatRoom roomNow = 
            ChatUtils.getOrCreateRoom(
                    Folder.nameRootChat, UserUtils.getUserAdmin()
            );
    
    
    public TerminalChat(TerminalType terminalType, User user) {
        super(terminalType, user);
        //addCommand(new CommandMkdir(this));
        //addCommand(new CommandUserSave(this));
    }

    @Override
    public String getDescription() {
        return "Chat with others";
    }

    @Override
    public String getIntro() {
        
        String title = "Chat";
        String text = TextFunctions.getWindowFrame(title);
        
        String intro = 
                paint(BLUE,
                    text
                );
        
        return intro;
    }

    @Override
    public String getName() {
        return "chat";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        ChatArchive archive = roomNow.getMessagesToday();
        return roomNow.sendChatText(user, commandInput);
    }

}
