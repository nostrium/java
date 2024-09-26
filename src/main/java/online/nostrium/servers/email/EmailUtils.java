/*
 * Utils related to our email handling
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.email;

import java.io.File;
import java.io.IOException;
import online.nostrium.user.User;
import online.nostrium.logs.Log;
import online.nostrium.servers.terminal.TerminalCode;
import org.apache.commons.io.FileUtils;

/**
 * Handle the folders related to email and users
 *
 *
 * @author Brito
 * @date: 2024-09-02
 * @location: Germany
 */
public class EmailUtils {

    public static final String 
            nameEmailFolder = "email", // root for email data
            nameInbox = "inbox", // messages arriving
            nameOutbox = "outbox", // messages to be sent away
            nameSent = "sent", // messages sent
            nameSpam = "spam", // spammy messages to delete after 30 days
            nameAttachments = "attach", // files received as attachments
            nameGarbage = "garbage";    // files deleted, removed after 30 days

    public static File getFolderEmail(User user, boolean createFolder) {
        File folderUser = user.getFolder(createFolder);
        File folder = new File(folderUser, nameEmailFolder);
        if (createFolder && folder.exists() == false) {
            try {
                FileUtils.forceMkdir(folder);
            } catch (IOException ex) {
                Log.write("EMAIL", TerminalCode.CRASH,
                        "Failed to create the email folder",
                        ex.getLocalizedMessage());
            }
        }
        return folder;
    }

    public static File getFolderEmail(User user, String folderName) {
        File folderEmail = getFolderEmail(user, true);
        File folder = new File(folderEmail, folderName);
        if (folder.exists() == false) {
            try {
                FileUtils.forceMkdir(folder);
            } catch (IOException ex) {
                Log.write("EMAIL", TerminalCode.CRASH,
                        "Failed to create the email folder",
                        ex.getLocalizedMessage());
            }
        }
        return folder;
    }
    
    public static void createFoldersBasic(User user) {
        EmailUtils.getFolderEmail(user, true);
        // create the other basic folders
        EmailUtils.getFolderEmail(user, EmailUtils.nameInbox);
        EmailUtils.getFolderEmail(user, EmailUtils.nameOutbox);
        EmailUtils.getFolderEmail(user, EmailUtils.nameSent);
        EmailUtils.getFolderEmail(user, EmailUtils.nameSpam);
        EmailUtils.getFolderEmail(user, EmailUtils.nameGarbage);
        EmailUtils.getFolderEmail(user, EmailUtils.nameAttachments);
    }

}
