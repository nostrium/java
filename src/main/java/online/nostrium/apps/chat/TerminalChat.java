/*
 * The app for chatting with other users
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.chat;

import java.util.ArrayList;
import online.nostrium.folder.FolderUtils;
import online.nostrium.main.core;
import online.nostrium.session.NotificationType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;
import online.nostrium.session.Session;
import online.nostrium.utils.TextFunctions;

/**
 * @author Brito
 * @date: 2024-08-06
 * @location: Germany
 */
public class TerminalChat extends TerminalApp {

    // load the room, but not the chat history
    public ChatRoom roomNow = null;

    @SuppressWarnings("LeakingThisInConstructor")
    public TerminalChat(Session session) {
        super(session);
        
        User userAdmin = UserUtils.getFakeUserAdmin();
        if (userAdmin != null) {
            roomNow = ChatUtils.getOrCreateRoom(this,
                FolderUtils.nameRootChat, UserUtils.getUserAdmin());
        }
        
        // let's overwrite the previous LS command
        removeCommand("ls");
        addCommand(new CommandChatLs(this, roomNow, session));
        addCommand(new CommandChatClear(this, session));


    }

    @Override
    public String getDescription() {
        return "Chat with others";
    }

    @Override
    public String getIntro() {

        String title = "Chat";
        String intro = session.getScreen().getWindowFrame(title);

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
    public String getIdName() {
        return "chat";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {

        if (roomNow == null) {
            // make sure the room is not empty
            roomNow = ChatUtils.getOrCreateRoom(this,
                FolderUtils.nameRootChat, UserUtils.getFakeUserAdmin());
        }
        
        // delete the current line before writing new stuff
        session.getScreen().deleteCurrentLine();

        // write the chat message on the room
        CommandResponse reply = roomNow.sendChatText(session.getUser(), commandInput);
        // when something went wrong, stop it here
        if (reply.getCode() != TerminalCode.OK) {
            return reply;
        }

        String line = createMessageLine(
                System.currentTimeMillis() / 1000L,
                session.getUser().getDisplayName(),
                reply.getText()
        );

        // write the new line
        session.getScreen().writeln(line);

        // send a notification
        core.sessions.sendNotification(
                this.getPathVirtual(),
                session.getUser(),
                NotificationType.UPDATE,
                line
        );

        return new CommandResponse(TerminalCode.OK, "");
    }

    /**
     * The message that appears on the chat box to the user
     *
     * @param timeCreated
     * @param userId
     * @param content
     * @return
     */
    public String createMessageLine(long timeCreated, String userId, String content) {
        String timestamp = TextFunctions.convertLongToDateTime(timeCreated);
//        timestamp = screen.paint(TerminalColor.BLUE, timestamp);
        String line = timestamp
                + " "
                + "["
                + userId
                + "]"
                + " "
                + content;
        return line;
    }

    @Override
    public void receiveNotification(User userSender, NotificationType notificationType, Object object) {
        if (notificationType != NotificationType.UPDATE) {
            return;
        }
        if (userSender.sameAs(session.getUser())) {
            return;
        }
        String line = (String) object;
        session.getScreen().deleteCurrentLine();

        // break the text in two parts
        int i = line.indexOf("]") + 1;
        String line1 = line.substring(0, i);
        String line2 = line.substring(i);

        // write the text in different speed
        session.getScreen().write(line1);
        session.getScreen().writeLikeHuman(line2, 25);

        session.getScreen().writeUserPrompt(session);
    }

    @Override
    public String getPathVirtual() {
        String path = session.getCurrentLocation().getPath()
                + "#"
                + roomNow.name;
        return path;
    }

}
