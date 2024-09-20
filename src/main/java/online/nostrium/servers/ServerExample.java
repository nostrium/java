/*
 * FTP server
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers;

import online.nostrium.main.core;

/**
 * @author Brito
 * @date: 2024-09-20
 * @location: Germany
 */
public class ServerExample extends Server {

    @Override
    public String getId() {
        return "Server_FTP";
    }

    @Override
    public int getPort() {
        if (core.config.debug) {
            return core.config.portFTP_Debug;
        } else {
            return core.config.portFTP;
        }
    }
    
    @Override
    public int getPortSecure() {
        if (core.config.debug) {
            return core.config.portFTPS_Debug;
        } else {
            return core.config.portFTPS;
        }
    }

    @Override
    protected void boot() {
        isRunning = true;
    }

    @Override
    protected void shutdown() {
    }

}
