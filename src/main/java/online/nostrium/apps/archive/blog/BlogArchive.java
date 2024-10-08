/*
 * Blog for the user
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.archive.blog;

import java.io.File;
import online.nostrium.apps.archive.Archive;
import online.nostrium.folder.FolderType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-09-24
 * @location: Germany
 */
public class BlogArchive extends Archive{

    public BlogArchive(String id, File folder, Session session) {
        super(id, folder, session);
        setType(FolderType.BLOG);
    }

    @Override
    public String getIntro() {
        return "Blog";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        return reply(TerminalCode.NOT_FOUND);
    }

    @Override
    public String getIdName() {
        return id;
    }

}
