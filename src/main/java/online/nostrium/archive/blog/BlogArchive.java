/*
 * Blog for the user
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.archive.blog;

import java.io.File;
import online.nostrium.archive.Archive;
import online.nostrium.folder.FolderType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.user.User;
import online.nostrium.utils.screens.Screen;

/**
 * @author Brito
 * @date: 2024-09-24
 * @location: Germany
 */
public class BlogArchive extends Archive{

    public BlogArchive(String id, File folder, Screen screen, User user) {
        super(id, folder, screen, user);
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
    public String getName() {
        return id;
    }

}
