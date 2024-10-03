/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.terminal;

import java.util.ArrayList;
import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public abstract class TerminalCommand {
    
    final public Session session;
    final public String name;

    final protected TerminalApp app;
    // force commands to use a slash
    public boolean requireSlash = true;
    public boolean internalCommand = false;
    
    // list of alternative commands that can be recognized
    @SuppressWarnings("unchecked")
    public ArrayList<String> 
            commandsAlternative = new ArrayList();
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public TerminalCommand(TerminalApp app, Session session) {
        this.session = session;
        this.app = app;
        this.name = commandName();
    }
    
    protected String paint(TerminalColor colorType, String text) {
        return session.getScreen().paint(colorType, text);
    }
    
    
    public abstract String commandName(); // lower case command name without spaces
    public abstract String oneLineDescription();
    public abstract CommandResponse execute(TerminalType terminalType, String parameters); // test to be run when calling this command

    public boolean hasCommand(String commandToFind){
        if(getPathWithName().equalsIgnoreCase(commandToFind)){
            return true;
        }
        // look inside the alternative commands
        if(requireSlash && commandToFind.startsWith("/")){
            commandToFind = commandToFind.substring(1);
        }
        // iterate all commands available
        for(String commandAlternative : commandsAlternative){
            if(commandAlternative.equalsIgnoreCase(commandToFind)){
                return true;
            }
        }
        return false;
    };
    
    /**
     * Provides the name that is used for calling this command
     * @return the name with slash when applicable
     */
    public String getPathWithName() {
        if(requireSlash == false){
            return commandName();
        }else{
            return "/" + commandName();
        }
    }

    protected CommandResponse reply(TerminalCode code, String text){
        return new CommandResponse(code, text);
    }
    
    protected CommandResponse reply(TerminalCode code){
        return new CommandResponse(code, "");
    }
    
    protected CommandResponse reply(TerminalApp app){
        return new CommandResponse(app);
    }
    
}
