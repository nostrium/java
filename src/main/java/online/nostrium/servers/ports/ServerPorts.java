/*
 * A set of ports for a specific service
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.ports;

import java.util.ArrayList;
import online.nostrium.main.core;

/**
 * @author Brito
 * @date: 2024-10-16
 * @location: Germany
 */
public class ServerPorts {

    ArrayList<ServerPort> ports = new ArrayList<>();

    
    /**
     * Returns a port that matches an ID.
     * Attention that it will also match the expected port
     * for both development or production environments.
     * @param portId
     * @return 
     */
    public int get(PortId portId){
        return get(portId.toString());
    }
    
    /**
     * Returns a port that matches an ID.
     * Attention that it will also match the expected port
     * for both development or production environments.
     * @param id
     * @return 
     */
    public int get(String id){
        for(ServerPort port : ports){
            if(port.id.equalsIgnoreCase(id) == false){
                continue;
            }
            // have a match
            if(core.config.debug){
                return port.portNumberDebug;
            }else{
                return port.portNumber;
            }
        }
        return -1;
    }
    
    
    public void add(ServerPort serverPort){
        ports.add(serverPort);
    }

    public ArrayList<ServerPort> getPorts() {
        return ports;
    }

    public String getInfo() {
        ArrayList<String> list = new ArrayList<>();
        for(ServerPort port : ports){
            String data = port.getId() + ":" + port.getPortNumber();
            list.add(data);
        }
        return list.toString();
    }
}
