/*
 *  Users registered on the forum
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.main.old.forum.structures;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.main.Folder;
import online.nostrium.utils.TextFunctions;
import org.apache.commons.io.FileUtils;

/**
 * Date: 2023-02-10
 * Place: Germany
 * @author brito
 */
public class ManageUsers {

    public static boolean hasUser(String id){
        File folder = Folder.getFolderUsers();
        id = TextFunctions.cleanString(id);
        File file = new File(folder, id + ".json");
        return file.exists();
    }
    
    @SuppressWarnings("deprecation")
    public static void addUser(ForumUser forumUser){
        String text = forumUser.jsonExport();
        String filename = forumUser.getFilename();
        File folder = Folder.getFolderUsers();
        File file = new File(folder, filename);
        try {
            FileUtils.writeStringToFile(file, text);
        } catch (IOException ex) {
            Logger.getLogger(ManageUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
