/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.basic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.session.Session;
import org.apache.commons.io.FileUtils;

/**
 * @author Brito
 * @date: 2024-10-11
 * @location: Germany
 */
public class CommandRead extends TerminalCommand {

    public CommandRead(TerminalApp app, Session session) {
        super(app, session);
        this.requireSlash = false;
        // add an alternative command
        this.commandsAlternative.add("print");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        // syntax
        // read filename.txt

        // get the base folder
        File folder = session.getCurrentLocation().getRelatedFolderOrFile();
        
        // create the filename
        String filename = parameters;
        File file = new File(folder, filename);

        if(file.exists() == false){
            return reply(TerminalCode.INVALID, "Invalid file");
        }
        
        try {
            String text = FileUtils.readFileToString(file, Charset.defaultCharset());
            session.getScreen().writeln(text);
        } catch (IOException ex) {
            Logger.getLogger(CommandRead.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return reply(TerminalCode.OK);

    }

    @Override
    public String commandName() {
        return "read";
    }

    @Override
    public String oneLineDescription() {
        return "Read a file";
    }

}
