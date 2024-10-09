/*
 * Defines an application
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import online.nostrium.folder.FolderUtils;
import online.nostrium.servers.terminal.AppData;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.session.NotificationType;
import online.nostrium.session.Session;
import online.nostrium.session.maps.MapApp;
import online.nostrium.session.maps.MapFolder;
import online.nostrium.session.maps.MapLink;
import online.nostrium.user.User;
import online.nostrium.utils.cybersec.Permissions;

/**
 * @author Brito
 * @date: 2024-10-08
 * @location: Germany
 */
public abstract class App {

    // define the session where the app is running
    public final Session session;
    // settings and data for this app
    public AppData dataUser;
    
    // is there a folder associated to this app?
    protected File relatedFolder = null;    

    // map for this app
    protected MapApp map = null;

    // navigation between different apps
    public TerminalApp appParent = null;
    @SuppressWarnings("unchecked")
    public ArrayList<App> appChildren = new ArrayList();
    public TreeSet<MapFolder> folders = new TreeSet<>();
    public TreeSet<MapLink> links = new TreeSet<>();
    public final Map<String, TerminalCommand> commands = new HashMap<>();

    // permissions to access this app
    public Permissions permissions = new Permissions(dataUser);

    public App(Session session) {
        this.session = session;
        // temporary data never saved to disk
        this.dataUser = new AppData(this);
    }

    public File getFolderCommonData() {
        File folderRoot = FolderUtils.getFolderData();
        return FolderUtils.defaultGetFolder(folderRoot, this.getIdName()
        );
    }

    // get a name that we can type on the CLI
    public abstract String getIdName();

    // provide a one-line description of the app
    public abstract String getDescription();

    public void setMap(MapApp map) {
        this.map = map;
    }
    
    public MapApp getMap() {
        return map;
    }

    public File getRelatedFolder() {
        return relatedFolder;
    }

    // shows a path id for this app
    public String getPathVirtual() {
        return map.getPath();
    }

    public void receiveNotification(
            User userSender,
            NotificationType notificationType,
            Object object) {

        // only text messages are supported for now
        if (object instanceof String dataReceived) {
            String text = "["
                    + notificationType.name()
                    + "] "
                    + dataReceived;

            session.getScreen().writeln(text);
        }
    }

    /**
     * A new command was received, is it a valid one?
     *
     * @param terminalType specific color capacities of terminal
     * @param commandInput
     * @return null when nothing relevant needs to be done
     */
    public CommandResponse handleCommand(
            TerminalType terminalType, String commandInput) {
        // needs to have something for processing
        if (commandInput == null || commandInput.length() == 0) {
            return null;
        }

        // don't accept commands too big for processing
        if (commandInput.length() > 16384) {
            return null;
        }

        String commandToProcess = commandInput.toLowerCase();
        String parameters = "";
        int pos = commandToProcess.indexOf(" ");
        // there is a space, get the command until the space
        if (pos > 0) {
            commandToProcess = commandToProcess.substring(0, pos);
            parameters = commandInput.substring(pos + 1);
        }

        // try to get this command
        TerminalCommand cmd = null;
        for (TerminalCommand command : commands.values()) {
            // found a command
            if (command.hasCommand(commandToProcess)) {
                cmd = command;
                break;
            }
        }

        //TerminalCommand cmd = commands.get(commandToProcess);
        if (cmd != null) {
            return cmd.execute(terminalType, parameters);
        } else {
            // command was not recognized, run the default
            return defaultCommand(commandInput);
        }
    }

    public abstract CommandResponse defaultCommand(String commandInput);
    
    // shows an intro for this app
    public abstract String getIntro();
}
