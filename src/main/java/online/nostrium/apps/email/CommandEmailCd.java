/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.email;

import java.io.File;
import online.nostrium.servers.email.EmailUtils;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.servers.terminal.TerminalUtils;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class CommandEmailCd extends TerminalCommand {

    public CommandEmailCd(TerminalEmail app) {
        super(app);
        this.requireSlash = false;
        // add an alternative command
        this.commandsAlternative.add("chdir");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        String text = "";
        
        // no need to continue when there are no parameters
        if(parameters == null || parameters.isEmpty()){
            return reply(TerminalCode.OK, text);
        }
        
        // shall we go down one directory?
        if(parameters.equalsIgnoreCase("..")){
            String subFolders = app.getSubFolders();
            if(subFolders.isEmpty() == false){
               File folder = (File) app.dataUser.get("folderCurrent");
               folder = folder.getParentFile();
                app.dataUser.put("folderCurrent", folder);
                return reply(TerminalCode.OK);
            }
            return reply(TerminalCode.EXIT_APP, "");
        }
        
        // move to the root directory
        if(parameters.equalsIgnoreCase("/")){
            TerminalApp appRoot = TerminalUtils.getAppRoot(this.app);
            return reply(appRoot);
        }
        
        
        File folderBase = EmailUtils.getFolderEmail(app.user, true);
        File folder = new File(folderBase, parameters);
        if(folder.exists() == false){
            return reply(TerminalCode.NOT_FOUND, "Folder not found");
        }
        
        // the folder exists, accept the change
        app.dataUser.put("folderCurrent", folder);
        
        return reply(TerminalCode.OK);
    }

    @Override
    public String commandName() {
        return "cd";
    }

    @Override
    public String oneLineDescription() {
        return "Change to another app";
    }

}
