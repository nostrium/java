/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.basic;

import java.util.TreeSet;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalColor;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.session.Session;
import online.nostrium.session.maps.Map;
import online.nostrium.session.maps.MapApp;
import online.nostrium.session.maps.MapFolder;

/**
 * @author Brito
 * @date: 2024-08-05
 * @location: Germany
 */
public class CommandLs extends TerminalCommand {

    public CommandLs(TerminalApp app, Session session) {
        super(app, session);
        this.requireSlash = false;
        // add an alternative command
        this.commandsAlternative.add("dir");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {

        TreeSet<Map> list = session.getCurrentLocation().listFiles(parameters);

        String text = "";
        for (Map map : list) {
            TerminalColor color = TerminalColor.GREEN;
            if(map instanceof MapApp || map instanceof MapFolder){
                color = TerminalColor.CYAN;
            }
            
            text += ""
                    + paint(color, map.getName())
                    + "\t";
        }

        return reply(TerminalCode.OK, text);
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
