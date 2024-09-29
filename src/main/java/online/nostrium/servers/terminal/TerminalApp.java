/*
 * Defines a terminal app
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.terminal;

import java.io.File;
import online.nostrium.apps.basic.CommandHelp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import online.nostrium.session.NotificationType;
import online.nostrium.apps.basic.CommandCd;
import online.nostrium.apps.basic.CommandExit;
import online.nostrium.apps.basic.CommandLs;
import online.nostrium.apps.chat.CommandChatClear;
import online.nostrium.utils.screens.Screen;
import online.nostrium.user.User;
import online.nostrium.logs.Log;
import online.nostrium.folder.FolderUtils;
import online.nostrium.utils.cybersec.Permissions;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public abstract class TerminalApp {

    // settings and data for this app
    public AppData dataUser;

    // navigation between different apps
    public TerminalApp appParent = null;
    @SuppressWarnings("unchecked")
    public ArrayList<TerminalApp> appChildren = new ArrayList();
    public User user;

    public final Map<String, TerminalCommand> commands = new HashMap<>();
    public final Screen screen;

    // permissions to access this app
    public Permissions permissions = new Permissions(dataUser);

    public TerminalApp(Screen screen, User user) {
        this.screen = screen;
        this.user = user;
        // temporary data never saved to disk
        this.dataUser = new AppData(this);
        // add the default commands
        addCommandInternal(new CommandHelp(this));
        addCommandInternal(new CommandLs(this));
        addCommandInternal(new CommandCd(this));
        addCommandInternal(new CommandChatClear(this));
        addCommandInternal(new CommandExit(this));

        // add the permissions into the data storage
//        data.put(namePermissions, permissions);
    }

    public final void addCommandInternal(TerminalCommand command) {
        command.internalCommand = true;
        addCommand(command);
    }

    public final void addApp(TerminalApp app) {
        app.appParent = this;
        this.appChildren.add(app);
    }

    // shows an intro for this app
    public abstract String getIntro();

    // shows an intro for this app
    public String getId() {
        String path = TerminalUtils.getPath(this);
        return path;
    }

    /**
     * Adds a new command, avoid duplicates
     *
     * @param command
     */
    protected final void addCommand(TerminalCommand command) {
        // avoid duplicates
        if (commands.containsKey(command.commandName())) {
            return;
        }
        commands.put(command.commandName(), command);
    }
    
    public String getSubFolders(){
        return "";
    }
    
    public void setSubFolders(String text){
    }

    protected final void removeCommand(String commandName) {
        // find the command when existing
        if (commands.containsKey(commandName) == false) {
            return;
        }
        // remove the command from our list
        commands.remove(commandName);

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

    public String paint(TerminalColor colorType, String text) {
        return screen.paint(colorType, text);
    }

    // provide a one-line description of the app
    public abstract String getDescription();

    public abstract CommandResponse defaultCommand(String commandInput);

    public abstract String getName();

    protected CommandResponse reply(TerminalCode code, String text) {
        return new CommandResponse(code, text);
    }

    protected CommandResponse reply(TerminalCode code) {
        return new CommandResponse(code, "");
    }

    protected CommandResponse reply(TerminalApp app) {
        return new CommandResponse(app);
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

            screen.writeln(text);
        }
    }

    /**
     * Update all apps with the new user info
     *
     * @param user
     */
    public void updateUser(User user) {
        TerminalApp rootApp = this;
        // travel to the root app
        if (this.appParent != null) {
            while (rootApp.appParent != null) {
                rootApp = rootApp.appParent;
            }
        }
        setNewUser(rootApp, user);
    }

    private void setNewUser(TerminalApp app, User user) {
        app.user = user;
        if (app.appChildren.isEmpty()) {
            return;
        }
        // change for all new cases
        for (TerminalApp appChild : app.appChildren) {
            setNewUser(appChild, user);
        }
    }

    public File getFolderCommonData() {
        File folderRoot = FolderUtils.getFolderData();
        return FolderUtils.defaultGetFolder(
                folderRoot, this.getName()
        );
    }

    public void log(TerminalCode code, String text, String data) {
        Log.write(this.getId(), code, text, data);
    }
    
    public File getFolderCurrent() {
        if(dataUser.has("folderCurrent") == false){
            return null;
        }
        String name = (String) dataUser.get("folderCurrent");
        File folder = new File(user.getFolder(false), name);
        return folder;
    }

    public void setFolderCurrent(File folderCurrent) {
        String text = "/";
        String folderRoot = user.getFolder(false).getAbsolutePath();
        String folderToAdd = folderCurrent.getAbsolutePath();
        if(folderRoot.length() < folderToAdd.length()){
            text = folderToAdd.substring(folderRoot.length());
        }
        dataUser.put("folderCurrent", text);
    }
}
