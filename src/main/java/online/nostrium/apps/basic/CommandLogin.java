/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.basic;

import online.nostrium.apps.user.User;
import online.nostrium.apps.user.UserUtils;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.utils.EncryptionUtils;
import static online.nostrium.utils.TextFunctions.sha256;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class CommandLogin extends TerminalCommand{

    public CommandLogin(TerminalApp app) {
        super(app);
        this.requireSlash = false;
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
        // login USERNAME password
        String[] value = parameters.split(" ");
        
        // syntax needs to be correct
        if(value.length != 2){
            return reply(TerminalCode.FAIL, "Wrong syntax.\n"
                    + "Please use as syntax: login <username> <password>");
        }
        
        // get the username and password values
        
        String username = value[0];
        String password = value[1];
        
        // just look at the hashed version of the password
        String passwordHash = sha256(password);
        
        // get the related user
        User user = UserUtils.getUserByUsername(username);
        
        if(user == null){
            return reply(TerminalCode.NOT_FOUND, "User name was not found");
        }
        
        if(user.hasPassword() == false){
            return reply(TerminalCode.FAIL, "User did not define password. Only login with NSEC is possible");
        }
        
        // compare the password
        if(user.getPasswordHash().equalsIgnoreCase(passwordHash) == false){
            return reply(TerminalCode.FAIL, "Wrong password");
        }
        
        // password accepted, set the password
        user.setPassword(password);
        
        // decrypt the nsec
        String nsecDec = EncryptionUtils.decrypt(user.getNsecEncrypted(), password);
        user.setNsec(nsecDec);
        
        // update the user info
        this.app.updateUser(user);
        
        // all done
        return reply(TerminalCode.OK, "Logged with ");
    }

    @Override
    public String commandName() {
        return "login";
    }
    
    @Override
    public String oneLineDescription() {
        return "Log as existing user";
    }


}
