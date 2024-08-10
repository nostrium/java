/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.chat;

import java.util.ArrayList;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalColor;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.users.User;
import online.nostrium.users.UserUtils;
import online.nostrium.utils.TextFunctions;

/**
 * @author Brito
 * @date: 2024-08-08
 * @location: Germany
 */
public class CommandChatLs extends TerminalCommand {

    final ChatRoom room;
    
    public CommandChatLs(TerminalApp app, ChatRoom room) {
        super(app);
        this.room = room;
        this.requireSlash = false;
        // add an alternative command
        this.commandsAlternative.add("dir");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        // get all the message
        @SuppressWarnings("unchecked")
        ArrayList<String> lines = new ArrayList();
        //ArrayList<ChatMessage> messagesToday = room.getMessagesToday().messages;
        
        ArrayList<ChatMessage> messages = room.getLastMessages(50);
        
        for(ChatMessage message : messages){
            
            String id = message.pubkey.substring(0, 4);
            
            User user = UserUtils.getUser(message.pubkey);
            if(user != null && user.getDisplayName() != null){
                id = user.getDisplayName();
            }
            
            String content = TextFunctions.sanitizeChatMessage(message.content);
            
            
            String timestamp = 
                    TextFunctions.convertLongToDateTime(message.createdAt);
            
            timestamp =
                    app.screen.paint(TerminalColor.BLUE, timestamp);
            
            String line = timestamp
                    + " "
                    + "["
                    + id
                    + "]"
                    + " "
                    + content
                    ;
            
            lines.add(line);
        }
        
        String output = "";
        for(String line : lines){
            output += line + "\n";
        }
        
        // remove the last break line
        if(output.length() > 0){
            output = output.substring(0, output.length() -1);
        }

        return reply(TerminalCode.OK, output);
    }

    @Override
    public String commandName() {
        return "ls";
    }

    @Override
    public String oneLineDescription() {
        return "List the previous messages";
    }

}
