/*
 * Utils related to our email handling
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.email;

import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import static com.icegreen.greenmail.util.ServerSetup.PROTOCOL_SMTP;
import jakarta.mail.Address;
import jakarta.mail.Header;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.user.User;
import online.nostrium.logs.Log;
import online.nostrium.servers.terminal.TerminalCode;
import org.apache.commons.io.FileUtils;

/**
 * Handle the folders related to email and users
 *
 *
 * @author Brito
 * @date: 2024-09-02
 * @location: Germany
 */
public class EmailUtils {

    public static final String 
            nameEmailFolder = "email", // root for email data
            nameInbox = "inbox", // messages arriving
            nameOutbox = "outbox", // messages to be sent away
            nameSent = "sent", // messages sent
            nameSpam = "spam", // spammy messages to delete after 30 days
            nameAttachments = "attach", // files received as attachments
            nameGarbage = "garbage";    // files deleted, removed after 30 days

    public static File getFolderEmail(User user, boolean createFolder) {
        File folderUser = user.getFolder(createFolder);
        File folder = new File(folderUser, nameEmailFolder);
        if (createFolder && folder.exists() == false) {
            try {
                FileUtils.forceMkdir(folder);
            } catch (IOException ex) {
                Log.write("EMAIL", TerminalCode.CRASH,
                        "Failed to create the email folder",
                        ex.getLocalizedMessage());
            }
        }
        return folder;
    }

    public static File getFolderEmail(User user, String folderName) {
        File folderEmail = getFolderEmail(user, true);
        File folder = new File(folderEmail, folderName);
        if (folder.exists() == false) {
            try {
                FileUtils.forceMkdir(folder);
            } catch (IOException ex) {
                Log.write("EMAIL", TerminalCode.CRASH,
                        "Failed to create the email folder",
                        ex.getLocalizedMessage());
            }
        }
        return folder;
    }
    
    public static void createFoldersBasic(User user) {
        EmailUtils.getFolderEmail(user, true);
        // create the other basic folders
        EmailUtils.getFolderEmail(user, EmailUtils.nameInbox);
        EmailUtils.getFolderEmail(user, EmailUtils.nameOutbox);
        EmailUtils.getFolderEmail(user, EmailUtils.nameSent);
        EmailUtils.getFolderEmail(user, EmailUtils.nameSpam);
        EmailUtils.getFolderEmail(user, EmailUtils.nameGarbage);
        EmailUtils.getFolderEmail(user, EmailUtils.nameAttachments);
    }

    public static EmailMessage convertEmail(MimeMessage raw){
        
        EmailMessage message = new EmailMessage();
        
        try {
            // add the headers
            Enumeration<Header> iterator = raw.getAllHeaders();
            HashMap<String, String> headers = message.getHeaders();
            while(iterator.hasMoreElements()){
                Header header = iterator.nextElement();
                headers.put(header.getName(), header.getValue());
            }
        } catch (MessagingException ex) {
            Logger.getLogger(EmailUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            // sender of this email
            message.setFrom(raw.getFrom()[0].toString());
            
            // add the receivers
            ArrayList<String> receivers = new ArrayList<>();
            for(Address address : raw.getRecipients(Message.RecipientType.TO)){
                String emailAddress = address.toString();
                if(receivers.contains(emailAddress)){
                    continue;
                }
                receivers.add(emailAddress);
            }
            message.setToUsers(receivers);
            
            // body of the message
            String body = GreenMailUtil.getBody(raw);
            if(body != null){
                message.setBody(body);
            }
            
            // other topics
            if(raw.getSentDate()!= null){
                message.setTimeCreated(raw.getSentDate().getTime());
            }
            message.setReadByReceiver(false);
            if(raw.getSubject() != null){
                message.setTitle(raw.getSubject());
            }
            message.setTimeReceived(System.currentTimeMillis());
            
        } catch (MessagingException ex) {
            Logger.getLogger(EmailUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return message;
    }
    
    public static void sendEmail(EmailMessage msg, ServerSetup server){
        GreenMailUtil.sendTextEmail(
                msg.getToUsers(), msg.getFrom(),
        msg.getTitle(), msg.getBody(), server
        );
    }

    /**
     * Writes the message to the user folders
     * @param message
     * @param user 
     */
    public static void writeEmail(MimeMessage message, User user) {
        
        // create the basic folders when they don't exist yet
        EmailUtils.createFoldersBasic(user);
        
        // get the inbox folder
        File folderInbox = EmailUtils.getFolderEmail(user, EmailUtils.nameInbox);
        if(folderInbox.exists() == false){
            return;
        }
        
        try {
            
            EmailMessage msg = EmailUtils.convertEmail(message);
            String text = msg.jsonExport();
            String filename = generateFilename(message);
            File file = new File(folderInbox, filename);

            FileUtils.writeStringToFile(file, text, Charset.defaultCharset());
            
            Log.write("EMAIL", TerminalCode.OK, "Received email", 
                    "From: "
                            + Arrays.toString(message.getFrom())
                            + " -> To: "
                            + user.getNpub()
            );
            
//            System.out.println("Received email from: " + Arrays.toString(message.getFrom()));
//            System.out.println("Subject: " + message.getSubject());
//            System.out.println("Body: " + GreenMailUtil.getBody(message));
            
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(EmailUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static String generateFilename(MimeMessage message) {
        // Get the current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        String dateTime = dateFormat.format(new Date());

        String subject = null;

        try {
            // Try to get the subject
            subject = message.getSubject();

            // If subject is null or empty, try to get the first few words from the body
            if (subject == null || subject.trim().isEmpty()) {
                Object content = message.getContent();
                if (content instanceof String string) {
                    subject = string;
                    if (subject.length() > 30) {
                        subject = subject.substring(0, 30); // Limit body-based title to 30 characters
                    }
                }
            }
        } catch (MessagingException | IOException e) {
            Log.write("EMAIL", TerminalCode.CRASH, 
                    "Filename create", e.getLocalizedMessage());
        }

        // If subject is still null or empty, use "NoTitle"
        if (subject == null || subject.trim().isEmpty()) {
            subject = "NoTitle";
        }

        // Remove special characters and spaces from subject
        subject = subject.replaceAll("[^a-zA-Z0-9]", "_");

        String filename = dateTime + "_" + subject;
        filename = filename.replace("__", "_");
        if(filename.endsWith("_")){
            filename = filename.substring(0, filename.length()-1);
        }
        
        // Construct the final filename
        return filename + ".json";
    }
    
    
}
