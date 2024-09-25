/*
 * Panel for system administration
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.email;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.servers.email.EmailUtils;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.utils.screens.Screen;
import online.nostrium.user.User;
import static online.nostrium.servers.email.EmailUtils.createFoldersBasic;
import online.nostrium.servers.terminal.TerminalUtils;

/**
 * @author Brito
 * @date: 2024-09-02
 * @location: Germany
 */
public class TerminalEmail extends TerminalApp {
    
    File folderBase = null;
    

    public TerminalEmail(Screen screen, User user) {
        super(screen, user);
         // add apps inside
        this.removeCommand("ls");
        this.addCommand(new CommandEmailLs(this));
        
        this.removeCommand("cd");
        this.addCommand(new CommandEmailCd(this));
        
        folderBase = EmailUtils.getFolderEmail(user);
        setFolderCurrent(EmailUtils.getFolderEmail(user));
        
//        permissions.clearEveryone();
//        permissions.denyUserType(UserType.ANON);
        
        createFoldersBasic(user);
    }

    @Override
    public String getDescription() {
        return "Send and receive emails";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        return reply(TerminalCode.NOT_FOUND);
    }

    @Override
    public String getIntro() {
        screen.writeln(screen.getWindowFrame("Email"));
        return "Type 'help' to see the available commands";
    }

    @Override
    public String getName() {
        return "email";
    }
    
    @Override
    public String getSubFolders(){
        String result = "";
        try {
            String pathBase = this.folderBase.getCanonicalPath();
            String pathCurrent = this.getFolderCurrent().getCanonicalPath();
            result = pathCurrent.substring(pathBase.length());
        } catch (IOException ex) {
            Logger.getLogger(TerminalEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    

    // shows an intro for this app
    @Override
    public String getId(){
        String path = TerminalUtils.getPath(this);
        return path;
    }

    public File getFolderCurrent() {
        if(data.has("folderCurrent") == false){
            return null;
        }
        return (File) data.get("folderCurrent");
    }

    public void setFolderCurrent(File folderCurrent) {
        data.put("folderCurrent", folderCurrent);
    }
    

}
