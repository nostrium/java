/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.chat;

import java.util.ArrayList;
import online.nostrium.main.Folder;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.servers.apps.user.User;
import online.nostrium.servers.apps.user.UserUtils;
import online.nostrium.utils.TextFunctions;

/**
 * @author Brito
 * @date: 2024-08-08
 * @location: Germany
 */
public class CommandChatLs extends TerminalCommand {

    final ChatRoom room;
    
    public CommandChatLs(TerminalChat app, ChatRoom room) {
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
            
            String userId = UserUtils.getUserDisplayNameBasic(message.pubkey);
            
            User user = UserUtils.getUserByNpub(message.pubkey);
            if(user != null && user.getDisplayName() != null){
                userId = user.getDisplayName();
            }
            
            String content = TextFunctions.sanitizeChatMessage(message.content);
            
            // create the line of previous messages
            String line = ((TerminalChat) app).createMessageLine(
                    message.created_at, 
                    Folder.nameAnonUsers
                            + "#" + userId, 
                    content
            );
            
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
