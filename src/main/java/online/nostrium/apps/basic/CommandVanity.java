/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.basic;

import online.nostrium.user.User;
import online.nostrium.user.UserUtils;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.utils.TextFunctions;

/**
 * @author Brito
 * @date: 2024-08-11
 * @location: Germany
 */
public class CommandVanity extends TerminalCommand {

    public CommandVanity(TerminalApp app) {
        super(app);
        this.requireSlash = false;
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        
        String output = "";
        
        // clean out unusable characters
        parameters = TextFunctions.cleanString(parameters);
        
    // Check if parameters are valid and consist of exactly three uppercase letters
    if (TextFunctions.isValidText(parameters) == false 
            || parameters.length() != 3 
            || !parameters.matches("[A-Z]+")) {
        return reply(TerminalCode.INCOMPLETE, "Please add three uppercase letters as parameter");
    }
        
        // the parameters are now usable
        long maxTries = 1_000_000_000;
        long counter = 0;
        long updateCount = 0;
        app.screen.writeln("Looking for NPUB starting with: " + parameters);
        while(maxTries > counter){
            counter++;
            User user = UserUtils.createUserAnonymous();
            String name = user.getDisplayName();
            if(name.toLowerCase().endsWith(parameters.toLowerCase())){
                output += "npub: " + user.getNpub();
                output += "\n";
                output += "nsec: " + user.getNsec();
                return reply(TerminalCode.OK,  output);
            }
            
            // output some status
            if(counter > updateCount + 1000){
                updateCount = counter;
                app.screen.deleteCurrentLine();
                app.screen.write("Tries: " + counter);
            }
            
        }

        return reply(TerminalCode.OK,  "Unable to find one");
    }

    @Override
    public String commandName() {
        return "vanity";
    }

    @Override
    public String oneLineDescription() {
        return "Generate a vanity three-letter user name";
    }


}
