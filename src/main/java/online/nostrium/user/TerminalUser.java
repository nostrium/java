/*
 * The root app
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.user;

import java.io.File;
import online.nostrium.apps.archive.blog.BlogArchive;
import online.nostrium.apps.archive.forum.ForumArchive;
import online.nostrium.apps.email.TerminalEmail;
import online.nostrium.session.NotificationType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.session.Session;
import online.nostrium.session.maps.MapFolder;
import online.nostrium.session.maps.MapLink;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class TerminalUser extends TerminalApp {

    public TerminalUser(Session session) {
        super(session);
                
        File folder = session.getUser().getFolder(false);
        
        BlogArchive appBlog = new BlogArchive("blog", folder, session);
        addApp(appBlog);
        
        ForumArchive appForum = new ForumArchive("forum", folder, session);
        addApp(appForum);
        
        // add the email for this user
        addApp(new TerminalEmail(session));
        
        
        // add a folder that does not need to exist unless used, but should be listed
        File folderToAdd = new File(session.getUser().getFolder(false), "public");
        MapFolder mapFolder = addFolder(folderToAdd);
        // add the link the forum and blog?
        // this makes sure the blog is reachable both on the private area
        // for writing blog posts and on the public www server area for reading
        MapLink indexBlog = new MapLink("blog", appBlog);
        mapFolder.addLink(indexBlog);
        
        
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
    public String getIdName() {
        return "user";
    }

    @Override
    public void receiveNotification(User userSender, NotificationType notificationType, Object object) {
        session.getScreen().writeln("Received a notification");
    }

    @Override
    public String getPathVirtual() {
        return this.getMap().getPath();
    }

}
