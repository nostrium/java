/*
 * Server for receiving email
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.email;

import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import online.nostrium.servers.Server;

import java.util.Arrays;
import java.util.List;
import online.nostrium.servers.ports.PortId;
import online.nostrium.servers.ports.PortType;
import online.nostrium.servers.ports.ServerPort;

/**
 * To send emails from the command line in Linux, try something like:
 *
 * sendemail -f example@example.com -t admin@nostrium.online -u "Test title" -m
 * "This is a test email" -s 127.0.0.1:2500 -xu your-email@example.com -xp
 * your-email-password
 *
 * 
sendemail -f example@example.com -t brito@nostrium.net -u "Test title" -m
 * "This is a test email" -s 127.0.0.1:2500 -xu your-email@example.com -xp
 * your-email-password



 *
 *
 * @author Brito
 * @date: 2024-08-31
 * @location: Germany
 */
public class ServerEmail extends Server {

    private GreenMail greenMail;

    @Override
    public String getId() {
        return "Server_Email";
    }
    
    @Override
    public void setupPorts() {
        // add SMTP
        ServerPort port = new ServerPort(PortId.SMTP.toString(),
                PortType.NONENCRYPTED,
                PortId.SMTP.getPortNumber(),
                PortId.SMTP_Debug.getPortNumber()
        );
        ports.add(port);
    }


    public ServerSetup getServerSetup() {
        int port = ports.get(PortId.SMTP);

        return new ServerSetup(port,
                "0.0.0.0", ServerSetup.PROTOCOL_SMTP);
    }

    @Override
    protected void boot() {

        greenMail = new GreenMail(getServerSetup());
        greenMail.start();
        //System.out.println("SMTP Server started on port: " + getPort());
        isRunning = true;

        // Start a thread to monitor incoming emails
        startEmailMonitoring();
    }

    @SuppressWarnings("Convert2Lambda")
    private void startEmailMonitoring() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        // Wait for new messages
                        if (greenMail.waitForIncomingEmail(3000, 1)) { // Wait for at least one email
                            handleIncomingEmails();
                        }
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void shutdown() {
        if (greenMail != null) {
            greenMail.stop();
        }
    }

    private void handleIncomingEmails() {
        try {
            // Retrieve all messages
            List<MimeMessage> messages = Arrays.asList(greenMail.getReceivedMessages());
            for (MimeMessage message : messages) {
                // Process each email
                EmailProcess.receive(message);
            }

            // Clear processed emails from GreenMail's storage to avoid reprocessing
            greenMail.purgeEmailFromAllMailboxes();
        } catch (FolderException e) {
            e.printStackTrace();
        }
    }

}
