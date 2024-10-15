/*
 * Send and receive emails
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.email;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.main.core;
import online.nostrium.session.NotificationType;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;

/**
 * @author Brito
 * @date: 2024-10-10
 * @location: Germany
 */
public class EmailProcess {

    /**
     * Receive emails from the outside to this server
     *
     * @param message
     */
    public static void receive(MimeMessage message) {
        try {
            // get a list of the email destinations
            ArrayList<User> users = addUsers(message);
            
            // no people to receive messages? Nothing to be done
            if(users.isEmpty()){
                return;
            }

            // for each user, write the message
            for(User user : users){
                EmailUtils.writeEmail(message, user);
            }
         
        } catch (MessagingException ex) {
            Logger.getLogger(ServerEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static ArrayList<User> addUsers(MimeMessage message) throws MessagingException {
        ArrayList<String> userEmails = new ArrayList<>();
        Address[] list = message.getRecipients(Message.RecipientType.TO);
        if (list != null) {
            for (Address address : list) {
                userEmails.add(address.toString());
            }
        }
        list = message.getRecipients(Message.RecipientType.CC);
        if (list != null) {
            for (Address address : list) {
                userEmails.add(address.toString());
            }
        }
        // produce the list of users
        ArrayList<User> users = new ArrayList<>();
        String domainName = "@" + core.config.getDomain();
        
        for(String userEmail : userEmails){
            // check that they have the expected domain name
            if(userEmail.endsWith(domainName) == false){
                continue;
            }
            // now try to find the user inside our system
            String username = userEmail.substring(0, userEmail.length() - domainName.length());
            User user = UserUtils.getUserByNameOrNpub(username);
            // only accept valid users
            if(user == null){
                continue;
            }
            // avoid duplicate users
            boolean dontAdd = false;
            for(User userExisting : users){
                if(userExisting.sameAs(user)){
                    dontAdd = true;
                }
            }
            if(dontAdd){
                continue;
            }
            users.add(user);
        }
        
        return users;
    }

}
