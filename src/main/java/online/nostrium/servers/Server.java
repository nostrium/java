/*
 *  Define a server
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers;

import online.nostrium.utils.time;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public abstract class Server {

    protected boolean keepRunning = false;
    protected boolean isRunning = false;
    
    public abstract String getId();
    
    public abstract int getPort();

    protected abstract void boot();

    public void start() {

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
            String text = getId() + " started on port " + getPort();
            System.out.println(text);
        }
    }

    public void stop() {
        keepRunning = false;
        String text = getId() + " stopped on port " + getPort();
        System.out.println(text);
        shutdown();
    }

    public boolean isRunning() {
        return isRunning;
    }

    protected abstract void shutdown();

}
