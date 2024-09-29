/*
 * Defines a command written inside a text terminal
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.admin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.folder.FolderUtils;
import online.nostrium.main.core;
import static online.nostrium.main.core.config;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.session.Session;
import online.nostrium.utils.screens.Screen;
import online.nostrium.utils.cybersec.LetsEncryptDomainRegistration;
import org.apache.commons.io.FileUtils;

/**
 * @author Brito
 * @date: 2024-08-30
 * @location: Germany
 */
public class CommandRegisterSSL extends TerminalCommand{

    public CommandRegisterSSL(TerminalApp app, Session session) {
        super(app, session);
        this.requireSlash = false;
    }

    @Override
    public CommandResponse execute(TerminalType terminalType, String parameters) {
       
        
        String domain = config.getDomain();
        String email = "admin@" + domain;
        File folderCerts = FolderUtils.getFolderCerts();
        File folderWellKnown = FolderUtils.getFolderWellKnown();
        File folderAcmeChallenge = new File(folderWellKnown, "acme-challenge");
        try {
            FileUtils.forceMkdir(folderAcmeChallenge);
        } catch (IOException ex) {
            Logger.getLogger(CommandRegisterSSL.class.getName()).log(Level.SEVERE, null, ex);
        }
        // trial run or full run
        boolean useStaging = core.config.debug;
        
        // output the setting being used
        Screen screen = session.getScreen();
        screen.writeln(screen.getWindowFrame("SSL registration"));
        screen.writeln("domain: " + domain);
        screen.writeln("email: " + email);
        screen.writeln("Folder certs: " + folderCerts.getPath());
        screen.writeln("Folder .well-known: " + folderWellKnown.getPath());
        screen.writeln("Folder challenge: " + folderAcmeChallenge.getPath());
        screen.writeln("Debug/testing mode: " + useStaging);
        screen.writeln("---------------------");
        
        
        LetsEncryptDomainRegistration registrar = 
            new LetsEncryptDomainRegistration(
                    domain, 
                    email, 
                    folderCerts, 
                    folderAcmeChallenge, 
                    useStaging,
                    screen
            );
        
        boolean success = registrar.registerDomain();
        if (success) {
            return reply(TerminalCode.OK, "Domain registration and certificate issuance successful!");
        } else {
            return reply(TerminalCode.OK, "Domain registration failed.");
        }
        
    }

    @Override
    public String commandName() {
        return "ssl";
    }
    
    @Override
    public String oneLineDescription() {
        return "Install or renew the SSL certificate";
    }


}
