/*
 * Server for receiving email
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.email;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import online.nostrium.main.core;
import online.nostrium.servers.Server;

/**
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
    public int getPort() {
        if (core.config.debug) {
            return core.config.portSMTP_Debug;
        } else {
            return core.config.portSMTP;
        }
    }

    @Override
    protected void boot() {
        ServerSetup setup = new ServerSetup(getPort(), null, ServerSetup.PROTOCOL_SMTP);
        greenMail = new GreenMail(setup);
        greenMail.start();
        //System.out.println("SMTP Server started on port: " + getPort());
        isRunning = true;
    }

    @Override
    public void shutdown() {
        if (greenMail != null) {
            greenMail.stop();
            //System.out.println("SMTP Server stopped.");
        }
    }
}
