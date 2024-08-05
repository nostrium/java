/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package network.nostrium.servers.terminal;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public abstract class TerminalCommand {

    final TerminalApp app;
    // force commands to use a slash
    public boolean requireSlash = true;
    
    public TerminalCommand(TerminalApp app) {
        this.app = app;
    }
    
    protected String paint(TerminalColor colorType, String text) {
        return TerminalUtils.paint(app.terminalType, colorType, text);
    }
    
    
    public abstract String commandName(); // lower case command name without spaces
    public abstract String oneLineDescription();
    public abstract CommandResponse execute(TerminalType terminalType, String parameters); // test to be run when calling this command

    /**
     * Provides the name that is used for calling this command
     * @return the name with slash when applicable
     */
    public String getName() {
        if(requireSlash == false){
            return commandName();
        }else{
            return "/" + commandName();
        }
    }

    protected CommandResponse reply(int code, String text){
        return new CommandResponse(code, text);
    }
    
}
