/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.admin;

import java.util.ArrayList;
import online.nostrium.logs.Log;
import online.nostrium.logs.LogItem;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.session.Session;
import online.nostrium.utils.TextFunctions;

/**
 * @author Brito
 * @date: 2024-08-30
 * @location: Germany
 */
public class CommandAdminLog extends TerminalCommand {

    public CommandAdminLog(TerminalApp app, Session session) {
        super(app, session);
        this.requireSlash = false;
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        
        if(parameters.isEmpty()){
            session.getScreen().writeln("Showing the most recent log items:");
            ArrayList<LogItem> items = Log.getListLastItems(100);
            for(LogItem item : items){
                session.getScreen().writeln(getText(item));
            }
            return reply(TerminalCode.OK, "sup");
        }
        return reply(TerminalCode.OK, "sup");
    }
    
    private String getText(LogItem item) {
        String text = ""
                + ""
                + TextFunctions.convertLongToDateTimeWithSeconds(item.getTimestamp())
                + " ["
                + item.getCode().name()
                + "] "
                + item.getId()
                + " | "
                + item.getText()
                + ": "
                + item.getData();
        return text;
    }

    @Override
    public String commandName() {
        return "log";
    }

    @Override
    public String oneLineDescription() {
        return "Show the logs";
    }


}
