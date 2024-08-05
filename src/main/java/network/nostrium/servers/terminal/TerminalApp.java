/*
 * Defines a terminal app
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package network.nostrium.servers.terminal;
import java.util.HashMap;
import java.util.Map;
import network.nostrium.servers.apps.basic.CommandExit;
import network.nostrium.servers.apps.basic.CommandLs;

/**

 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public abstract class TerminalApp {

    public TerminalApp appParent = null;
    
    public final Map<String, TerminalCommand> commands = new HashMap<>();
    public final TerminalType terminalType;

    public TerminalApp(TerminalType terminalType) {
        this.terminalType = terminalType;
        // add the default commands
        addCommandInternal(new CommandHelp(this));
        addCommandInternal(new CommandLs(this));
        addCommandInternal(new CommandExit(this));
    }

    public final void addCommandInternal(TerminalCommand command){
        command.internalCommand = true;
        addCommand(command);
    }
    
    // shows an intro for this app
    public abstract String getIntro();
    
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
        for(TerminalCommand command : commands.values()){
            if(command.hasCommand(commandToProcess)){
                cmd = command;
                break;
            }
        }
        
        //TerminalCommand cmd = commands.get(commandToProcess);
        if (cmd != null) {
            return cmd.execute(terminalType, parameters);
        } else {
            // command was not recognized, run the default
            return defaultCommand().execute(terminalType, parameters);
        }
    }

    public String paint(TerminalColor colorType, String text) {
        return TerminalUtils.paint(terminalType, colorType, text);
    }

    // provide a one-line description of the app
    public abstract String getDescription();
    
    public abstract TerminalCommand defaultCommand();
}
