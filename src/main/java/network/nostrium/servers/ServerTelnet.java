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
import static network.nostrium.servers.terminal.TerminalColor.GREEN_BRIGHT;
import network.nostrium.servers.terminal.TerminalType;
import static network.nostrium.servers.terminal.TerminalUtils.paint;
import network.nostrium.users.User;
import network.nostrium.users.UserUtils;

/**
 *
 * To test the telnet server do this from the command line:
 *
 * telnet localhost 10101
 *
 *
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
@SuppressWarnings("StaticNonFinalUsedInInitialization")
public class ServerTelnet {

    private static final int PORT = 10101;

    // create the basic CLI
    public static TerminalBasic terminalBasic = new TerminalBasic(TerminalType.ANSI);

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
            TerminalApp app = terminalBasic;
            User user = UserUtils.createUserAnonymous();

            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // do the intro
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                out.println(app.getIntro());
                writeUserPrompt(user, out);

                String inputLine;
                
                while ((inputLine = in.readLine()) != null) {
                    
                    System.out.println("Received: " + inputLine);
                    CommandResponse response = app
                            .handleCommand(TerminalType.ANSI, inputLine);

                    // ignore null responses
                    if (response == null) {
                        // output the next prompt
                        writeUserPrompt(user, out);
                        continue;
                    }

                    // is it time to leave?
                    if (response.getCode() == -1) {
                        out.println(response.getText());
                        break;
                    }

                    // output the reply
                    if (response.getText().length() > 0) {
                        // output the message
                        out.println(response.getText());
                    }

                    // output the next prompt
                    writeUserPrompt(user, out);
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
    
    

    public static void writeUserPrompt(User user, PrintWriter out) {
        String userPrompt = paint(TerminalType.ANSI, GREEN_BRIGHT,
                user.getDisplayName())
                + ":/> ";
        out.print(userPrompt);
        out.flush();
    }

}
