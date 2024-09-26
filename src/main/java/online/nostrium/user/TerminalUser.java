/*
 * The root app
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.user;

import java.io.File;
import online.nostrium.archive.blog.BlogArchive;
import online.nostrium.archive.forum.ForumArchive;
import online.nostrium.servers.terminal.notifications.NotificationType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalUtils;
import online.nostrium.utils.screens.Screen;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class TerminalUser extends TerminalApp {

    public TerminalUser(Screen screenAssigned, User user) {
        super(screenAssigned, user);
                
        File folder = user.getFolder(false);
        
        addApp(new BlogArchive("blog", folder, screen, user));
        addApp(new ForumArchive("forum", folder, screen, user));
        
        addCommand(new CommandUserShow(this));
        //addCommand(new CommandUserPassword(this));
        //addCommand(new CommandUserSave(this));
        addCommand(new CommandUserSet(this));
    }

    @Override
    public String getDescription() {
        return "User functions (e.g. login, save, edit)";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        return reply(TerminalCode.NOT_FOUND);
    }

    @Override
    public String getIntro() {
        
        String title = "User space";
        String text = screen.getWindowFrame(title);
        return text;
    }

    @Override
    public String getName() {
        return "user";
    }

    @Override
    public void receiveNotification(User userSender, NotificationType notificationType, Object object) {
        screen.writeln("Received a notification");
    }

    @Override
    public String getId() {
        String path = TerminalUtils.getPath(this);
        return path;
    }

}
