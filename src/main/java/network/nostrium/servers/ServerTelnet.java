/*
 * Runs a telnet server
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package network.nostrium.servers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import network.nostrium.servers.apps.basic.TerminalBasic;
import network.nostrium.servers.terminal.CommandResponse;
import network.nostrium.servers.terminal.TerminalApp;
import network.nostrium.servers.terminal.TerminalCode;
import static network.nostrium.servers.terminal.TerminalColor.GREEN_BRIGHT;
import network.nostrium.servers.terminal.TerminalType;
import network.nostrium.servers.terminal.TerminalUtils;
import static network.nostrium.servers.terminal.TerminalUtils.paint;
import network.nostrium.users.User;
import network.nostrium.users.UserUtils;

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

    private static final int PORT = 10101;

    
    public static void main(String[] args) {
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
            // start with the basic app
            User user = UserUtils.createUserAnonymous();
            TerminalApp app = new TerminalBasic(TerminalType.ANSI, user);

            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // do the intro
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                out.println(app.getIntro());
                writeUserPrompt(app, user, out);

                String inputLine;
                
                while ((inputLine = in.readLine()) != null) {
                    
                    System.out.println("Received: " + inputLine);
                    CommandResponse response = app
                            .handleCommand(TerminalType.ANSI, inputLine);

                    // ignore null responses
                    if (response == null) {
                        // output the next prompt
                        writeUserPrompt(app, user, out);
                        continue;
                    }

                    // is it time to leave?
                    if (response.getCode() == TerminalCode.EXIT_CLIENT) {
                        out.println(response.getText());
                        break;
                    }
                    
                    // is it time to go down one app?
                    if (response.getCode() == TerminalCode.EXIT_APP && app.appParent != null) {
                        app = app.appParent;
                    }
                    
                     // is it time to change apps?
                    if (response.getCode() == TerminalCode.CHANGE_APP) {
                        app = response.getApp();
                        if(app.appParent != null){
                            out.println(app.getIntro());
                        }
                    }
                    

                    // output the reply
                    if (response.getText().length() > 0) {
                        // output the message
                        out.println(response.getText());
                    }

                    // output the next prompt
                    writeUserPrompt(app, user, out);
                }
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
    
    

    public static void writeUserPrompt(TerminalApp app, User user, PrintWriter out) {
        
        String path = TerminalUtils.getPath(app);
        
        String userPrompt = paint(TerminalType.ANSI, GREEN_BRIGHT,
                user.getDisplayName())
                + ":"
                + path
                + "> ";
        out.print(userPrompt);
        out.flush();
    }

}
