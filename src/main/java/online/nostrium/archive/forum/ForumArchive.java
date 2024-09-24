/*
 *  Forum for the user
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.archive.forum;

import online.nostrium.archive.blog.*;
import java.io.File;
import online.nostrium.archive.Archive;
import online.nostrium.archive.ArchiveType;

/**
 * @author Brito
 * @date: 2024-09-24
 * @location: Germany
 */
public class ForumArchive extends Archive{

    public ForumArchive(String id, File folder) {
        super(id, folder);
        setType(ArchiveType.FORUM);
    }

}
