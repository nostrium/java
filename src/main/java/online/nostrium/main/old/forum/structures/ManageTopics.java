/*
 *  Topics available on the forum
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.main.old.forum.structures;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.folder.FolderUtils;
import online.nostrium.utils.FileFunctions;
import org.apache.commons.io.FileUtils;

/**
 * Date: 2023-02-10
 * Place: Germany
 * @author brito
 */
public class ManageTopics {

    /**
     * Try to find a specific topic id inside
     * the forum database.
     * @param id
     * @return null when it does not exist
     */
    public static File getTopicFile(String id){
        File folder = FolderUtils.getFolderBaseForum();
        return FileFunctions.searchForFile(folder, id + "-", ".json");
    }
    
    public static ForumTopic getForumTopic(String id){
        File file = getTopicFile(id);
        if(file == null || file.exists() == false || file.length() == 0){
            return null;
        }
        return ForumTopic.jsonImport(file);
    }

    @SuppressWarnings("deprecation")
    public static void writeTopic(ForumTopic topic) {
        File file = getTopicFile(topic.getId());
        String text = topic.jsonExport();
        try {
            FileUtils.writeStringToFile(file, text);
        } catch (IOException ex) {
            Logger.getLogger(ManageTopics.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
