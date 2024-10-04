/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.basic;

import online.nostrium.main.core;
import online.nostrium.user.User;
import online.nostrium.user.UserType;
import online.nostrium.user.UserUtils;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.session.Session;
import online.nostrium.utils.EncryptionUtils;
import static online.nostrium.utils.TextFunctions.sha256;
import online.nostrium.utils.events.EventIndex;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class CommandRegister extends TerminalCommand{

    public CommandRegister(TerminalApp app, Session session) {
        super(app, session);
        this.requireSlash = false;
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        
        // only valid for anon users
        User user = session.getUser();
        if(user.getUserType() != UserType.ANON
                && user.getUserType() != UserType.ADMIN){
            return reply(TerminalCode.FAIL, 
                    "Can only use this command on new accounts");
        }
            

        // login USERNAME password
        String[] value = parameters.split(" ");
        
        // syntax needs to be correct
        if(value.length != 2){
            return reply(TerminalCode.FAIL, "Wrong syntax.\n"
                    + "Please use as syntax: register <username> <password>");
        }
        
        
        // get the username and password values
        
        String username = value[0];
        String password = value[1];
        
        
        if(UserUtils.isValidPassword(password) == false){
            return reply(TerminalCode.FAIL, "Invalid password");
        }
        
        if(UserUtils.isValidUsername(username) == false){
            return reply(TerminalCode.FAIL, "Invalid user name");
        }
        
        
        // just look at the hashed version of the password
        String passwordHash = sha256(password);
        
        // get the related user
        User userExists = UserUtils.getUserByUsername(username);
        
        if(userExists != null){
            return reply(TerminalCode.DENIED, "User name is already used");
        }
        
        
        // password accepted, set the password
        user.setPassword(password);
        user.setPasswordHash(passwordHash);
        user.setDisplayName(username);
        user.setUsername(username);
        if(user.getUserType() != UserType.ADMIN){
            user.setUserType(UserType.MEMBER);
        }
        
        // decrypt the nsec
        String nsecDec = EncryptionUtils.encrypt(user.getNsec(), password);
        user.setNsecEncrypted(nsecDec);
        
        // update the user info
        user.save();
        session.setUser(user);
        
        if(user.getUserType() == UserType.ADMIN){
            session.getScreen().writeln(UserType.ADMIN.toString() + " account is created.");
        }
        
        // mark a good registration
        core.events.triggerAfter(EventIndex.register, session);
        
        
        // all done
        return reply(TerminalCode.OK, "You are now registered");
    }

    @Override
    public String commandName() {
        return "register";
    }
    
    @Override
    public String oneLineDescription() {
        return "Register the current user";
    }


}
