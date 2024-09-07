/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.basic;

import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import static online.nostrium.utils.JarUtils.getBuildTime;
import static online.nostrium.utils.JarUtils.getVersion;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class CommandAbout extends TerminalCommand{

    public CommandAbout(TerminalApp app) {
        super(app);
        this.requireSlash = false;
    }

    private void writeln(String line){
        app.screen.writeln(line);
    }
    
    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
       
        String buildTime = getBuildTime();
        writeln("Build Time: " + buildTime);
        writeln("Additional info: https://github.com/nostrium/java");
//        writeln("-----------------");
//        writeln("");
//        String version = getVersion();
//        app.screen.writeln("Version: " + version);
        
        return reply(TerminalCode.OK);
    }

    @Override
    public String commandName() {
        return "about";
    }
    
    @Override
    public String oneLineDescription() {
        return "Info about the platform";
    }


}
