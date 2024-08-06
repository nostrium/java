/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.basic;

import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import static online.nostrium.servers.terminal.TerminalColor.BLUE;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalType;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class CommandExit extends TerminalCommand{

    public CommandExit(TerminalApp app) {
        super(app);
        this.commandsAlternative.add("return");
        this.commandsAlternative.add("quit");
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        // -10 means to leave the app, go to previous app
        int returnCode = TerminalCode.EXIT_APP;
        String text = "";
        // -100 means to exit the program
        if(app.appParent == null){
            returnCode = TerminalCode.EXIT_CLIENT;
            text = paint(BLUE, "Goodbye!");
        }
        return reply(returnCode, text);
    }

    @Override
    public String commandName() {
        return "exit";
    }

    @Override
    public String oneLineDescription() {
        return "Exits the app";
    }

}
