/*
 *  Data structure for a forum site
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package network.nostrium.forum.structures;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.nostrium.main.Folder;
import org.apache.commons.io.FileUtils;


/**
 * Date: 2023-02-09
 * Place: Germany
 * @author brito
 */
public class Forum {

    String id,
           title,
           one_line_summary;
    
    ArrayList<ForumGroup> forumGroups = new ArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOne_line_summary() {
        return one_line_summary;
    }

    public void setOne_line_summary(String one_line_summary) {
        this.one_line_summary = one_line_summary;
    }

    public ArrayList<ForumGroup> getForumGroups() {
        return forumGroups;
    }

    public void addForumGroup(ForumGroup forumGroup) {
        this.forumGroups.add(forumGroup);
    }

    /**
     * Create the folders for each forum group
     */
    public void createFolders() {
        for(ForumGroup forumGroup : forumGroups){
            createFolder(forumGroup);
        }
    }

    /**
     * Create a folder for each forum group
     * @param forumGroup 
     */
    private void createFolder(ForumGroup forumGroup) {
        File folder = new File(Folder.getFolderBaseForum(), 
                forumGroup.getFolderName());
        // create the folder when it does not exist
        if(folder.exists() == false){
            try {
                FileUtils.forceMkdir(folder);
            } catch (IOException ex) {
                Logger.getLogger(Forum.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
