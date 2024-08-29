/*
 *  Groups related topics inside a category
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.main.old.forum.structures;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.main.Folder;
import online.nostrium.utils.Log;
import online.nostrium.utils.TextFunctions;
import org.apache.commons.io.FileUtils;

/**
 * Date: 2023-02-08
 * Place: Germany
 *
 * @author brito
 */
public class ForumGroup {

    String id,
            title,
            one_line_summary,
            headerInfo,
            iconImage;

    ForumGroup forumParent;

    @SuppressWarnings("unchecked")
    ArrayList<String> forumTopics = new ArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getFolderName() {
        return id + "-" + TextFunctions.cleanString(title);
    }

    public String print() {
        return id + " -> " + title + " | " + getFolderName();
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

    public String getHeaderInfo() {
        return headerInfo;
    }

    public void setHeaderInfo(String headerInfo) {
        this.headerInfo = headerInfo;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public ForumGroup getForumParent() {
        return forumParent;
    }

    public void setForumParent(ForumGroup forumParent) {
        this.forumParent = forumParent;
    }

    public File getFolder() {
        return new File(Folder.getFolderBaseForum(), this.getFolderName());
    }

    public ArrayList<String> getForumTopics() {
        return forumTopics;
    }

    /**
     * Writes a topic on disk
     *
     * @param forumTopic
     */
    public void saveTopicToDisk(ForumTopic forumTopic, int folderId) {
        File folderBase = getFolder();
        File folderTopic = new File(folderBase, folderId + "");
        try {
            if (folderTopic.exists() == false) {
                FileUtils.forceMkdir(folderTopic);
            }
            File file = new File(folderTopic, forumTopic.getFilename() + ".json");
            String text = forumTopic.jsonExport();
            FileUtils.writeStringToFile(file, text);
            Log.write("Wrote topic: " + file.getPath());
        } catch (IOException ex) {
            Logger.getLogger(ForumGroup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
