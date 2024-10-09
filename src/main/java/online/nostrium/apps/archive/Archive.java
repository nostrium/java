/*
 * Defines the start for an email box, forum or blog
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.archive;

import online.nostrium.folder.FolderType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.apps.archive.commands.CommandArchiveRead;
import online.nostrium.apps.archive.commands.CommandArchiveWrite;
import online.nostrium.logs.Log;
import online.nostrium.folder.FolderUtils;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.session.Session;
import org.apache.commons.io.FileUtils;

/**
 * Make a structure that be used in parallel for:
 *  + forum
 *  + blog
 *  + HN/Reddit-style conversations
 *  + email
 *  + NNTP
 * 
 * @author Brito
 * @date: 2024-09-22
 * @location: Germany
 */
public abstract class Archive extends TerminalApp{
    
    
    private final 
        String filename = FolderUtils.nameFolderData;
    
    @Expose
    public final String id;      // small description without spaces and lower case
    
    @Expose
    String title;   // human readable title
    
    @Expose
    String description;  // one line description
    
    @Expose
    FolderType type = FolderType.NONE;
    
    public Archive(String id, File folder, Session session) {
        super(session);
        id = id.replace(" ", "").toLowerCase();
        this.id = id;
        this.relatedFolder = new File(folder, id);
        
        if(map != null){
            map.setRelatedFolderOrFile(relatedFolder);
        }
        
        //this.getMap().setRelatedFolderOrFile(folder);
        
//        if(folder.exists() == false){
//            try {
//                FileUtils.forceMkdir(folder);
//            } catch (IOException ex) {
//                Logger.getLogger(Archive.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        // add the base commands
        setFolderCurrent(folder);
        
        // list the folders/files
        //this.removeCommand("ls");
        //this.addCommand(new CommandArchiveLs(this, session));
        // delete a message
        // get inside a topic
        // write a new topic
        this.addCommand(new CommandArchiveWrite(this, session));
        this.addCommand(new CommandArchiveRead(this, session));
        // reply with a message
        // statistics
    }
    
     // Override toString() method
    @Override
    public String toString() {
        return this.id;
    }

    public FolderType getType() {
        return type;
    }

    public void setType(FolderType type) {
        this.type = type;
    }

    public void addGroup(Group group) {
        String idToAdd = group.getId();
        File folderGroup = new File(relatedFolder, idToAdd);
        if(folderGroup.exists() == false){
            try {
                FileUtils.forceMkdir(folderGroup);
            } catch (IOException ex) {
                Logger.getLogger(Archive.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        group.setFolder(folderGroup);
        // save the file inside
        group.save();
    }

    @Override
    public String getPathVirtual() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public Set<String> getGroups() {
        Set<String> list = new TreeSet<>();
        // list all files inside the folder
        File[] files = relatedFolder.listFiles();
        for(File file : files){
            if(file.isFile()){
                continue;
            }
            list.add(file.getName());
        }
        return list;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Export this object as JSON
     *
     * @return null if something went wrong
     */
    public String jsonExport() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                //.enableComplexMapKeySerialization()
                //.setLenient()
                .setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }

    public void save() {
        String text = this.jsonExport();
        File file = getFile();
        try {
            FileUtils.writeStringToFile(file, text, Charset.defaultCharset());
        } catch (IOException ex) {
            Log.write("ARCHIVE", TerminalCode.CRASH, "Write archive json", ex.getMessage());
        }
    }
    
    public File getFolder(){
        return relatedFolder;
    }
    
    @Override
    public String getSubFolders(){
        String result = "";
        try {
            String pathBase = relatedFolder.getCanonicalPath();
            String pathCurrent = this.getFolderCurrent().getCanonicalPath();
            if(pathBase.length() > pathCurrent.length()){
                result = "";
                setFolderCurrent(relatedFolder);
            }else{
                result = pathCurrent.substring(pathBase.length());
            }
        } catch (IOException ex) {
            Logger.getLogger(Archive.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public File getFile(){
        return new File(relatedFolder, filename);
    }
    
    
}
