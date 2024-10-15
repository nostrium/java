/*
 * Panel for system administration
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.email;

import com.icegreen.greenmail.util.ServerSetup;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.servers.email.EmailMessage;
import online.nostrium.servers.email.EmailUtils;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-09-02
 * @location: Germany
 */
public class TerminalEmail extends TerminalApp {

    File folderBase = null;
    public EmailMessage message = null;

    public TerminalEmail(Session session) {
        super(session);
        // add commands inside
        addCommand(new CommandEmailWrite(this, session));
    }

    @Override
    public String getDescription() {
        return "Send and receive emails";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {

        // is there a message to start writing?
        if (message == null) {
            return reply(TerminalCode.ROUTINE);
        }

        // stop the writing of this message
        if (commandInput.equalsIgnoreCase("stop")) {
            message = null;
            return reply(TerminalCode.ROUTINE, "Message was discarded");
        }
        
        // reset the writing of this message
        if (commandInput.equalsIgnoreCase("reset")) {
            message = new EmailMessage();
            session.getScreen().writeln("Message was reset, starting again from scratch.");
            session.getScreen().writeln("Please write a title for the email and press ENTER");
            return reply(TerminalCode.ROUTINE);
        }
        
        // stop the writing of this message
        if (commandInput.equalsIgnoreCase("send")) {
            EmailUtils.sendEmail(message, session);
            message = null;
            return reply(TerminalCode.ROUTINE, "Message was sent!");
        }

        // title portion
        if (message.getTitle() == null && commandInput.length() > 0) {
            message.setTitle(commandInput);
            session.getScreen().writeln("Using as title: " + commandInput);
            session.getScreen().writeln("Please write the destination email addresses:");
            return reply(TerminalCode.ROUTINE);
        }

        // message is not null, can start
        if (message.getTitle() == null) {
            session.getScreen().writeln("Write a title for the email and press ENTER");
            return reply(TerminalCode.ROUTINE);
        }

        // email addresses
        if (message.getToUsers().isEmpty() && commandInput.length() > 0) {
            ArrayList<String> list = new ArrayList<>();
            // don't accept spaces
            String dataList = commandInput.replace(" ", "");
            String[] addresses = dataList.split(",");
            for(String address : addresses){
                if(EmailUtils.isValidEmailAddress(address)){
                    list.add(address);
                }else{
                    session.getScreen().writeln("Invalid address: " + address);
                }
            }
            message.setToUsers(list);
            if(list.isEmpty() == false){
                session.getScreen().writeln("Please write the text of the message:");
            }
            return reply(TerminalCode.ROUTINE);
        }
        
        if (message.getToUsers().isEmpty()) {
            session.getScreen().writeln("Write the email addresses (comma separated when multiple)");
            return reply(TerminalCode.ROUTINE);
        }
        
        // write the body
         if (message.getBody() == null && commandInput.length() > 0) {
            message.setBody(commandInput);
            session.getScreen().writeln("Write SEND to conclude this message");
            return reply(TerminalCode.ROUTINE);
        }

        // message is not null, can start
        if (message.getBody() == null) {
            session.getScreen().writeln("Write the text for this email");
            return reply(TerminalCode.ROUTINE);
        }
        
        
        
        return reply(TerminalCode.NOT_FOUND);
    }

    @Override
    public String getIntro() {
        session.getScreen().writeln(
                session.getScreen().getWindowFrame("Email"));
        return "Type 'help' to see the available commands";
    }

    @Override
    public String getIdName() {
        return "email";
    }

    @Override
    public String getSubFolders() {
        String result = "";
        try {
            String pathBase = this.folderBase.getCanonicalPath();
            String pathCurrent = this.getFolderCurrent().getCanonicalPath();
            result = pathCurrent.substring(pathBase.length());
        } catch (IOException ex) {
            Logger.getLogger(TerminalEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    // shows an intro for this app
    @Override
    public String getPathVirtual() {
        return session.getCurrentLocation().getPath();
    }

}
