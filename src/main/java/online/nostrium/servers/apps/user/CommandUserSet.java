/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.user;

import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import static online.nostrium.servers.terminal.TerminalColor.GREEN;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.utils.TextFunctions;
import static online.nostrium.utils.TextFunctions.sha256;

/**
 * @author Brito
 * @date: 2024-08-05
 * @location: Germany
 */
public class CommandUserSet extends TerminalCommand{

    int minCharacters = 4;
    int maxCharacters = 64;
    int maxCharactersAbout = 256;
    
    public CommandUserSet(TerminalApp app) {
        super(app);
        this.requireSlash = false;
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        
        if(parameters.length() == 0 || parameters.contains(" ") == false){
           return reply(TerminalCode.FAIL, "Parameters are invalid");
        }
        
        String[] value = parameters.split(" ");
        
        String action = value[0];
        String text = parameters.substring(action.length() + 1);
        
        // get the user
        User user = this.app.user;
        
        return switch (action) {
            case "username" -> setUsername(user, text);
            case "about" -> setAbout(user, text);
            case "www" -> setWWW(user, text);
            case "password" -> setPassword(user, text);
            default -> reply(TerminalCode.FAIL, "Invalid action");
        };
        
    }

    @Override
    public String commandName() {
        return "set";
    }
    
    @Override
    public String oneLineDescription() {
        return "Set a value (username | about | www)";
    }

    private CommandResponse setUsername(User user, String text) {
        if(TextFunctions.isValidText(text) == false
                || text.length() > maxCharacters){
            return reply(TerminalCode.FAIL, "Invalid username");
        }
        
        String textCleaned = TextFunctions.cleanString(text);
        if(textCleaned.length() != text.length()){
            return reply(TerminalCode.FAIL, "Only alphanumeric characters are permitted");
        }
        
        if(text.contains(" ")){
            return reply(TerminalCode.FAIL, "User name cannot contain spaces");
        }
        
        // don't permit duplicate names
        User userSameUsername = UserUtils.getUserByUsername(text);
        if(userSameUsername != null){
            return reply(TerminalCode.FAIL, "User name already taken, please choose another one");
        }
        
        // set the user name
        user.setUsername(text);
        
        // no longer an anon, upgrade to member
        if(user.getUserType() == UserType.ANON){
            user.setUserType(UserType.MEMBER);
            user.setDisplayName(text);
        }
        
        // save the changes
        user.save();
        
        // all done
        return reply(TerminalCode.OK, "Done");
    }

    private CommandResponse setAbout(User user, String text) {
        if(TextFunctions.isValidText(text) == false
                || text.length() > maxCharactersAbout){
            return reply(TerminalCode.FAIL, "Text is not valid, or too large");
        }
        
        String textCleaned = TextFunctions.cleanString(text);
        if(textCleaned.length() != text.length()){
            return reply(TerminalCode.FAIL, "Only alphanumeric characters are permitted");
        }
        
        // write the text
        user.setAboutMe(text);
        user.save();        
        
        return reply(TerminalCode.OK, "Done");
    }

    
    private CommandResponse setWWW(User user, String text) {
        if(TextFunctions.isValidText(text) == false
                || text.length() > maxCharactersAbout){
            return reply(TerminalCode.FAIL, "Text is not valid, or too large");
        }
        
        // write the text
        user.setWebsite(text);
        user.save();        
        
        return reply(TerminalCode.OK, "Done");
    }

    public CommandResponse setPassword(User user, String parameters) {
         if(this.app.user.username == null){
            return reply(TerminalCode.INCOMPLETE, "Please define your username before setting a password");
        }
        
        if(parameters == null){
            return reply(TerminalCode.INCOMPLETE, "Please include a password");
        }

        // clean the password
        parameters = TextFunctions.sanitizePassword(parameters);
        
        if(parameters.isEmpty()){
            return reply(TerminalCode.INCOMPLETE, "Please include a password (minimum "
                    + minCharacters
                    + " characters)");
        }
        
        if(parameters.length() < minCharacters){
            return reply(TerminalCode.FAIL, "Too short, needs at minimum "
                    + minCharacters
                    + " characters");
        }
        
        if(parameters.length() > maxCharacters){
            return reply(TerminalCode.FAIL, "Too long password, max is "
                    + maxCharacters
                    + " characters");
        }
        
        // never store the password, just its hashed version
        String passwordHash = sha256(parameters);
        
        // change the password and save to disk
        user.setPasswordHash(passwordHash);
        user.save();
        
        return reply(TerminalCode.OK, "Done");
    }


}
