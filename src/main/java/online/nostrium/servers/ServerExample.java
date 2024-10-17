/*
 * FTP server
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers;

import online.nostrium.main.core;
import online.nostrium.servers.ports.PortId;
import online.nostrium.servers.ports.PortType;
import online.nostrium.servers.ports.ServerPort;

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
    public void setupPorts() {
        ServerPort port = new ServerPort(PortId.QOTD.toString(),
                PortType.NONENCRYPTED,
                PortId.QOTD.getPortNumber(),
                PortId.QOTD_Debug.getPortNumber()
        );
        ports.add(port);
    }
    

    @Override
    protected void boot() {
        isRunning = true;
    }

    @Override
    protected void shutdown() {
    }

}
