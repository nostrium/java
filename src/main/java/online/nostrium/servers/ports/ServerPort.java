/*
 * Defines a port open on the server
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.ports;

import online.nostrium.main.core;

/**
 * @author Brito
 * @date: 2024-10-16
 * @location: Germany
 */
public class ServerPort {

    final PortType portType;
    final int portNumber;
    final int portNumberDebug;
    final String id;
    boolean active = false;
    

    public ServerPort(String id, 
            PortType portType, 
            int portNumber,
            int portNumberDebug) {
        this.id = id;
        this.portType = portType;
        this.portNumber = portNumber;
        this.portNumberDebug = portNumberDebug;
    }

    public PortType getPortType() {
        return portType;
    }

    public int getPortNumber() {
        if(core.config.debug){
            return portNumberDebug;
        }else{
            return portNumber;
        }
    }

    public String getId() {
        return id;
    }

    public int getPortNumberDebug() {
        return portNumberDebug;
    }

    public boolean isActive() {
        return active;
    }
    
}
