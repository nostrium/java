/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.archive.commands;

import java.io.File;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import static online.nostrium.utils.ascii.DirectoryTreeBuilder.buildTree;

/**
 * @author Brito
 * @date: 2024-09-24
 * @location: Germany
 */
public class CommandArchiveLs extends TerminalCommand {

    public CommandArchiveLs(TerminalApp app) {
        super(app);
        this.requireSlash = false;
        // add an alternative command
        this.commandsAlternative.add("dir");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        // get the current folder
        File folder = (File) app.data.get("folderCurrent");
        String dirTree = buildTree(folder);
        
        // remove the last character
        if(dirTree.length() > 0){
            dirTree = dirTree.substring(0, dirTree.length()-1);
        }
        
        return reply(TerminalCode.OK, dirTree);
    }

    @Override
    public String commandName() {
        return "ls";
    }

    @Override
    public String oneLineDescription() {
        return "List the available items";
    }

}
