/*
 * Telegram bot connection
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.telegram;

import online.nostrium.main.Config;
import online.nostrium.main.core;
import online.nostrium.servers.Server;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

/**
 *
 * You need to fill up a test token inside config.json
 * 
 * @author Brito
 * @date: 2024-08-29
 * @location: Germany
 */
public class ServerTelegram extends Server {

    @Override
    public String getId() {
        return "Server_Telegram";
    }

    @Override
    public void setupPorts() {
    }
    
    @Override
    protected void boot() {
       
        try {
          String botToken = core.config.tokenTelegram;
          if(core.config.debug){
              botToken = core.config.tokenTelegram_Debug;
          }
        // Using try-with-resources to allow autoclose to run upon finishing
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new TelegramBot(botToken));
            isRunning = true;
            // Ensure this prcess wait forever
            Thread.currentThread().join();
             
        } catch (Exception e) {
            //e.printStackTrace();
        }
       } catch (Exception e) {
           //e.printStackTrace();
       }
    }
    

    @Override
    protected void shutdown() {
        isRunning = false;
    }

    public static void main(String[] args) {
        core.config = Config.loadConfig();
        ServerTelegram server = new ServerTelegram();
        server.boot();
    }

}
