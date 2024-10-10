/*
 * Defines a terminal app
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.terminal;

import java.io.File;
import online.nostrium.apps.basic.CommandAbout;
import online.nostrium.apps.basic.CommandHelp;
import online.nostrium.apps.basic.CommandCd;
import online.nostrium.apps.basic.CommandExit;
import online.nostrium.apps.basic.CommandHead;
import online.nostrium.apps.basic.CommandLL;
import online.nostrium.apps.basic.CommandLogin;
import online.nostrium.apps.basic.CommandLs;
import online.nostrium.apps.basic.CommandRead;
import online.nostrium.apps.basic.CommandRegister;
import online.nostrium.apps.basic.CommandStatus;
import online.nostrium.apps.basic.CommandTail;
import online.nostrium.apps.basic.CommandTime;
import online.nostrium.apps.basic.CommandTouch;
import online.nostrium.apps.basic.CommandTree;
import online.nostrium.apps.chat.CommandChatClear;
import online.nostrium.logs.Log;
import online.nostrium.servers.web.App;
import online.nostrium.session.Session;
import online.nostrium.session.maps.MapFolder;
import online.nostrium.session.maps.MapLink;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public abstract class TerminalApp extends App{

    public TerminalApp(Session session) {
        super(session);
        // temporary data never saved to disk
        this.dataUser = new AppData(this);
        // add the default commands
        addCommandInternal(new CommandHelp(this, session));
        addCommandInternal(new CommandLs(this, session));
        addCommandInternal(new CommandLL(this, session));
        addCommandInternal(new CommandCd(this, session));
        addCommandInternal(new CommandChatClear(this, session));
        addCommandInternal(new CommandTree(this, session));
        addCommandInternal(new CommandExit(this, session));

        addCommandInternal(new CommandRead(this, session));
        addCommandInternal(new CommandHead(this, session));
        addCommandInternal(new CommandTail(this, session));
        addCommandInternal(new CommandTouch(this, session));
        
        
        addCommandInternal(new CommandTime(this, session));
        addCommandInternal(new CommandStatus(this, session));
        //addCommand(new CommandVanity(this));
        addCommandInternal(new CommandLogin(this, session));
        addCommandInternal(new CommandRegister(this, session));
        
        addCommandInternal(new CommandAbout(this, session));

        // add the permissions into the data storage
//        data.put(namePermissions, permissions);
    }

    public final void addCommandInternal(TerminalCommand command) {
        command.internalCommand = true;
        addCommand(command);
    }

    public final void addApp(TerminalApp app) {
        app.appParent = this;
        this.appChildren.add(app);
    }
    
    public final MapFolder addFolder(File folder) {
        MapFolder mapFolder = new MapFolder(folder);
        folders.add(mapFolder);
        return mapFolder;
    }

    
    public final void addLink(String virtualPath, TerminalApp app) {
        MapLink mapLink = new MapLink(virtualPath, app);
        this.map.addLink(mapLink);
    }

    /**
     * Adds a new command, avoid duplicates
     *
     * @param command
     */
    protected final void addCommand(TerminalCommand command) {
        // avoid duplicates
        if (commands.containsKey(command.commandName())) {
            return;
        }
        commands.put(command.commandName(), command);
    }
    
    public String getSubFolders(){
        return "";
    }
    
    public void setSubFolders(String text){
    }

    protected final void removeCommand(String commandName) {
        // find the command when existing
        if (commands.containsKey(commandName) == false) {
            return;
        }
        // remove the command from our list
        commands.remove(commandName);

    }

    public String paint(TerminalColor colorType, String text) {
        return session.getScreen().paint(colorType, text);
    }

    protected CommandResponse reply(TerminalCode code, String text) {
        return new CommandResponse(code, text);
    }

    protected CommandResponse reply(TerminalCode code) {
        return new CommandResponse(code, "");
    }

    protected CommandResponse reply(TerminalApp app) {
        return new CommandResponse(app);
    }

    @Override
    public File getRelatedFolder() {
        return null;
    }

    public void log(TerminalCode code, String text, String data) {
        Log.write(this.getPathVirtual(), code, text, data);
    }
    
    public File getFolderCurrent() {
        if(dataUser.has("folderCurrent") == false){
            return null;
        }
        String name = (String) dataUser.get("folderCurrent");
        File folder = new File(session.getUser().getFolder(false), name);
        return folder;
    }

    public void setFolderCurrent(File folderCurrent) {
        String text = "/";
        String folderRoot = session.getUser().getFolder(false).getAbsolutePath();
        String folderToAdd = folderCurrent.getAbsolutePath();
        if(folderRoot.length() < folderToAdd.length()){
            text = folderToAdd.substring(folderRoot.length());
        }
        dataUser.put("folderCurrent", text);
    }
    
}
