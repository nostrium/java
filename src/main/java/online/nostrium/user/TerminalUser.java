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
import online.nostrium.session.NotificationType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.session.Session;
import online.nostrium.session.maps.MapFolder;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class TerminalUser extends TerminalApp {

    public TerminalUser(Session session) {
        super(session);
                
        File folder = session.getUser().getFolder(false);
        
        addApp(new BlogArchive("blog", folder, session));
        addApp(new ForumArchive("forum", folder, session));
        
        // add a folder that does not need to exist unless used, but should be listed
        File folderToAdd = new File(session.getUser().getFolder(false), "public");
        MapFolder mapFolder = addFolder(folderToAdd);
        // add the link the forum and blog?
        //User user = session.getUser();
        //mapFolder.index();
        
        addCommand(new CommandUserShow(this, session));
        addCommand(new CommandUserSet(this, session));
    }
    
    @Override
    public File getRelatedFolder() {
        return session.getUser().getFolder(false);
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
        String text = session.getScreen().getWindowFrame(title);
        return text;
    }

    @Override
    public String getPathWithName() {
        return "user";
    }

    @Override
    public void receiveNotification(User userSender, NotificationType notificationType, Object object) {
        session.getScreen().writeln("Received a notification");
    }

    @Override
    public String getId() {
        return this.getMap().getPath();
    }

}
