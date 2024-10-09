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
import java.util.List;
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
public class CommandTail extends TerminalCommand {

    public CommandTail(TerminalApp app, Session session) {
        super(app, session);
        this.requireSlash = false;
        // add an alternative command
        //this.commandsAlternative.add("print");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        // syntax
        // tail 10 filename.txt

        // get the base folder
        File folder = session.getCurrentLocation().getRelatedFolderOrFile();
        
        // e.g. -10 textfile.txt
        int anchor = parameters.indexOf(" "); // grab the first space
        if(anchor == -1){
            return reply(TerminalCode.INVALID, "Invalid syntax");
        }
        String valueText = parameters.substring(0, anchor);
        // create the filename
        String filename = parameters.substring(anchor+1);
        
        if(valueText.startsWith("-")){
            valueText = valueText.substring(1);
        }
        
        File file = new File(folder, filename);

        if(file.exists() == false){
            return reply(TerminalCode.INVALID, "Invalid file");
        }
        
        try {
            int value = Integer.parseInt(valueText);
            List<String> lines = FileUtils.readLines(file, Charset.defaultCharset());
            
            int pos = lines.size() - value;
            if(pos < 0){
                pos = 0;
            }
            
            for(int i = pos; i < lines.size(); i++){
                session.getScreen().writeln(lines.get(i));
            }
            
            
        } catch (IOException | NumberFormatException ex) {
            //Logger.getLogger(CommandHead.class.getName()).log(Level.SEVERE, null, ex);
            return reply(TerminalCode.INVALID, "Invalid syntax");
        }
        
        return reply(TerminalCode.OK);

    }

    @Override
    public String commandName() {
        return "tail";
    }

    @Override
    public String oneLineDescription() {
        return "Read N bottom lines from a file";
    }

}
