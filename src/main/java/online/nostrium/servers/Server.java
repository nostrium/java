/*
 *  Define a server
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers;

import online.nostrium.servers.ports.ServerPorts;
import online.nostrium.utils.time;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public abstract class Server {
    
    protected ServerPorts ports = new ServerPorts();

    protected boolean keepRunning = false;
    protected boolean isRunning = false;
    
    public abstract String getId();
    
    public abstract void setupPorts();
    
//    public abstract int getPort();
//    
//    public abstract int getPortSecure();
    
    public ServerPorts getPorts(){
        return ports;
    }
    

    protected abstract void boot();

    public void start() {
        // initialize the port configuration
        setupPorts();
        // launch the thread
        @SuppressWarnings("Convert2Lambda")
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                boot();
            }
        });

        // Start the thread
        thread.start();
        time.wait(1);
        if(this.isRunning){
            String text = getId() + " has started";
            if(getPorts().getPorts().isEmpty() == false){
                text += " on " + getPorts().getInfo();
            }else{
                text += " (no specific port)";
            }
            System.out.println(text);
        }
    }

    public void stop() {
        keepRunning = false;
        String text = getId() + " stopped on " + getPorts().getInfo();
        System.out.println(text);
        shutdown();
    }

    public boolean isRunning() {
        return isRunning;
    }

    protected abstract void shutdown();

}
