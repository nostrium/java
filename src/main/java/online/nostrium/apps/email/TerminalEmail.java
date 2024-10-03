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
import online.nostrium.servers.terminal.TerminalUtils;
import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-09-02
 * @location: Germany
 */
public class TerminalEmail extends TerminalApp {
    
    File folderBase = null;
    

    public TerminalEmail(Session session) {
        super(session);
         // add apps inside
        this.removeCommand("ls");
        this.addCommand(new CommandEmailLs(this, session));
        
        this.removeCommand("cd");
        this.addCommand(new CommandEmailCd(this, session));
        
        folderBase = EmailUtils.getFolderEmail(session.getUser(), false);
        setFolderCurrent(folderBase);
        
//        permissions.clearEveryone();
//        permissions.denyUserType(UserType.ANON);
        
//        createFoldersBasic(user);
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
        session.getScreen().writeln(
                session.getScreen().getWindowFrame("Email"));
        return "Type 'help' to see the available commands";
    }

    @Override
    public String getPathWithName() {
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
        return session.getCurrentLocation().getPath();
    }

    
    

}
