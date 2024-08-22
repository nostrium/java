/*
 * Runs a telnet server
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.terminal.telnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import online.nostrium.main.core;
import online.nostrium.notifications.ClientType;
import online.nostrium.notifications.Session;
import online.nostrium.servers.apps.basic.TerminalBasic;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.servers.terminal.screens.Screen;
import online.nostrium.servers.terminal.screens.ScreenTelnet;
import online.nostrium.servers.apps.user.User;
import online.nostrium.servers.apps.user.UserUtils;

/**
 *
 * To test the telnet server do this from the command line:
 *
 * telnet 127.0.0.1 10101
 *
 *
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
@SuppressWarnings("StaticNonFinalUsedInInitialization")
public class ServerTelnet {

    public static void startServerTelnet() {
        int PORT = 23;

        if (core.config.debug) {
            PORT = core.config.portTelnet_Debug;
        } else {
            PORT = core.config.portTelnet;
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Telnet server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Failed to start. "
                    + "Another service is already using port " + PORT);
        }
    }

    private static class ClientHandler implements Runnable {

        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {

            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                // the handler of text on the screen
                Screen screen = new ScreenTelnet(in, out);
                
                // start with the basic app
                User user = UserUtils.createUserAnonymous();
                TerminalApp app = new TerminalBasic(screen, user);
                
                Session session = new Session(ClientType.TELNET, app, user);
                core.sessions.addSession(session);

                // show the intro
                screen.writeIntro();
                
                // write the start prompt
                screen.writeUserPrompt(app);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {

                    System.out.println("Received: " + inputLine);
                    // show that the session is still active
                    session.ping();
                    
                    // handle the command request
                    CommandResponse response = app
                            .handleCommand(TerminalType.ANSI, inputLine);

                    // ignore null responses
                    if (response == null) {
                        // output the next prompt
                        screen.writeUserPrompt(app);
                        continue;
                    }

                    // is it time to leave?
                    if (response.getCode() == TerminalCode.EXIT_CLIENT) {
                        out.println(response.getText());
                        break;
                    }
                    
                    // forced session break
                    if (session.isTimeToStop()) {
                        break;
                    }
                    
                    

                    // is it time to go down one app?
                    if (response.getCode() == TerminalCode.EXIT_APP && app.appParent != null) {
                        app = app.appParent;
                    }

                    // is it time to change apps?
                    if (response.getCode() == TerminalCode.CHANGE_APP) {
                        app = response.getApp();
                        session.setApp(app);
                        if (app.appParent != null) {
                            out.println(app.getIntro());
                        }
                    }

                    // output the reply
                    if (response.getText().length() > 0) {
                        // output the message
                        out.println(response.getText());
                    }

                    // output the next prompt
                    screen.writeUserPrompt(app);
                }
                
                // close the session
                core.sessions.removeSession(session);
                
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Client disconnected: " + clientSocket.getInetAddress());
            }
        }

    }


}