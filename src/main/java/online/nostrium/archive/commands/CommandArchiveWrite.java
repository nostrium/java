/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.archive.commands;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.archive.Archive;
import online.nostrium.archive.ArchiveUtils;
import online.nostrium.archive.Markdown;
import online.nostrium.archive.Markdown.Topic;
import online.nostrium.folder.FolderData;
import online.nostrium.folder.FolderType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.session.Session;
import org.apache.commons.io.FileUtils;

/**
 * @author Brito
 * @date: 2024-09-25
 * @location: Germany
 */
public class CommandArchiveWrite extends TerminalCommand {

    public CommandArchiveWrite(Archive app, Session session) {
        super(app, session);
        this.requireSlash = false;
        // add an alternative command
        //this.commandsAlternative.add("");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {

        // get the current folder
        String folderName = (String) app.dataUser.get("folderCurrent");
        File folder = new File(session.getUser().getFolder(false), folderName);
        
        // syntax
        // write title -> content of topic
        
        // get the base folder
        Archive appArchive = (Archive) app;
        File appFolder = appArchive.getFolder();
        
        // create the folder data
        FolderData folderData;
        if(FolderData.doesNotExit(appFolder)){
            folderData = new FolderData(appFolder, FolderType.BLOG, true); 
        }else{
            folderData = FolderData.importFile(appFolder);
        }
        
         // folder needs to exist
        if(folder.exists() == false){
            try {
                
                FileUtils.forceMkdir(folder);
                
            } catch (IOException ex) {
                Logger.getLogger(CommandArchiveWrite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        String data[] = parameters.split(" -> ");
        
        if(data.length != 2){
            return reply(TerminalCode.INVALID, "Invalid syntax. Example of usage: "
                        + "write Title -> Content to write");
        }

        String title = data[0];
        String content = data[1];
        
        // create the filename
        String filename = ArchiveUtils.generateFilename(title) + ".md";
        File file = new File(folder, filename);
        
        Markdown md = new Markdown();
        Topic topic = md.getTopic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setDate();
        topic.setAuthor(session.getUser().getDisplayName());
        topic.setNpub(session.getUser().getNpub());
        
        try {
            md.saveToFile(file);
        } catch (IOException ex) {
            Logger.getLogger(CommandArchiveWrite.class.getName()).log(Level.SEVERE, null, ex);
            return reply(TerminalCode.CRASH, "Failed to write: " + filename);
        }
        return reply(TerminalCode.OK, "Written to disk: " + filename);
        
    }

    @Override
    public String commandName() {
        return "write";
    }

    @Override
    public String oneLineDescription() {
        return "Write a new topic";
    }


    

}
