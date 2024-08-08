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
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;

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
        // what we will write
        String text = "";
        
        // get all the message
        ArrayList<ChatMessage> messagesToday = room.getMessagesToday().messages;
        for(int i = messagesToday.size(); i < 0; i--){
            text = messagesToday.get(i) + "\n" + text;
        }
        
        

        return reply(TerminalCode.OK, text);
    }

    @Override
    public String commandName() {
        return "ls";
    }

    @Override
    public String oneLineDescription() {
        return "List the available items";
    }

}
