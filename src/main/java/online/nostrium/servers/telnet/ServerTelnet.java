/*
 * Runs a telnet server
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.telnet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import online.nostrium.main.core;
import online.nostrium.session.ChannelType;
import online.nostrium.session.Session;
import online.nostrium.apps.basic.TerminalBasic;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.TerminalType;
import online.nostrium.utils.screens.Screen;
import online.nostrium.user.UserUtils;
import online.nostrium.servers.Server;
import online.nostrium.servers.ports.PortId;
import online.nostrium.servers.ports.PortType;
import online.nostrium.servers.ports.ServerPort;
import online.nostrium.session.SessionUtils;

/**
 *
 * To test the telnet server do this from the command line:
 *
 * telnet 127.0.0.1 23000
 *
 *
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
@SuppressWarnings("StaticNonFinalUsedInInitialization")
public class ServerTelnet extends Server {

    private ServerSocket serverSocket;
    
    @Override
    protected void boot() {
        int PORT = ports.get(PortId.Telnet);
        try {
            serverSocket = new ServerSocket(PORT);
            isRunning = true;
            keepRunning = true;

            while (keepRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println(getId() + " failed to start. Another service is already using port " + PORT);
        }
    }

    @Override
    public void setupPorts() {
        ServerPort port = new ServerPort(PortId.Telnet.toString(),
                PortType.NONENCRYPTED,
                PortId.Telnet.getPortNumber(),
                PortId.Telnet_Debug.getPortNumber()
        );
        ports.add(port);
    }

    @Override
    public String getId() {
        return "Server_Telnet";
    }

    @Override
    protected void shutdown() {
        keepRunning = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Telnet Server shut down successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isRunning = false;
    }

    public static void main(String[] args) {
        core.startConfig();
        ServerTelnet server = new ServerTelnet();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        server.boot();
    }

    private static class ClientHandler implements Runnable {

        private final Socket clientSocket;
        private final List<String> commandHistory = new ArrayList<>();
        private int historyIndex = -1;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {

            try (
                InputStream in = clientSocket.getInputStream();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
//                
//// IAC DO LINEMODE (255, 253, 34)
//out.print((char) 255);
//out.print((char) 253);
//out.print((char) 34);
//
//// IAC SB LINEMODE MODE 0 IAC SE (255, 250, 34, 1, 0, 255, 240)
//out.print((char) 255);
//out.print((char) 250);
//out.print((char) 34);
//out.print((char) 1);
//out.print((char) 0);
//out.print((char) 255);
//out.print((char) 240);
//
//// IAC WILL ECHO (255, 251, 1)
//out.print((char) 255);
//out.print((char) 251);
//out.print((char) 1);
//
//// Ensure the data is flushed out to the client
//out.flush();


                // use the IP address as sessionId
                String sessionId = clientSocket.getInetAddress().toString();
                Screen screen = new ScreenTelnet(in, out);
                
                // get the corresponding session
                Session session = 
                        SessionUtils.getOrCreateSession(
                                ChannelType.TELNET, sessionId, screen);
                
               // the handler of text on the screen, always needs to be updated
                session.setScreen(screen);
                // start with the basic app
                TerminalApp app1 = new TerminalBasic(session);
                session.setup(app1, session.getUser(), screen);
                

                // show the intro
                screen.writeIntro();
                
                // is this the first user ever? Make him admin
                UserUtils.checkFirstTimeSetup(session.getUser(), screen);

                // write the start prompt
                screen.writeUserPrompt(session);

                int inputChar;
                StringBuilder inputBuffer = new StringBuilder();
                boolean skipNextNewline = false;

                while ((inputChar = in.read()) != -1) {

                    char receivedChar = (char) inputChar;

                    // react to autocomplete
                    if(receivedChar == '\t'){
                        session.getScreen().write("autocomplete");
                        continue;
                    }
                    
                    
                    // Handle arrow keys (escape sequences)
                    if (receivedChar == '\u001B') {  // Start of escape sequence
                        int nextChar1 = in.read();
                        if (nextChar1 == '[') {
                            int nextChar2 = in.read();
                            if (nextChar2 == 'A') {  // Up arrow key
                                if (historyIndex > 0) {
                                    historyIndex--;
                                    String textCommand = commandHistory.get(historyIndex);
                                    inputBuffer.setLength(0);
                                    inputBuffer.append(textCommand);
                                    // Clear the entire line and move cursor to start
                                    screen.deleteCurrentLine();
                                    screen.deletePreviousLine();
                                    screen.writeUserPrompt(session);
                                    out.print(inputBuffer.toString() + "\n");
                                    out.flush();
                                }
                                continue;
                            } else if (nextChar2 == 'B') {  // Down arrow key
                                if (historyIndex < commandHistory.size() - 1) {
                                    historyIndex++;
                                    inputBuffer.setLength(0);
                                    inputBuffer.append(commandHistory.get(historyIndex));
                                    // Clear the entire line and move cursor to start
                                    screen.deletePreviousLine();
                                    screen.deleteCurrentLine();
                                    screen.writeUserPrompt(session);
                                    out.print(inputBuffer.toString());
                                    out.flush();
                                } else {
                                    historyIndex = commandHistory.size();
                                    inputBuffer.setLength(0);
                                    screen.deleteCurrentLine();
                                    screen.writeUserPrompt(session);
                                }
                                continue;
                            }
                        }
                        // Skip any remaining characters in the escape sequence
                        continue;
                    }

                    // Skip processing if the last character was a carriage return and the current one is a newline
                    if (skipNextNewline && receivedChar == '\n') {
                        skipNextNewline = false;
                        continue;
                    }

                    // Accumulate characters until a line end is detected
                    if (receivedChar != '\n' && receivedChar != '\r') {
                        // Continue to accumulate characters
                        inputBuffer.append(receivedChar);
                        continue;
                    }

                    // If a carriage return is detected, set the flag to skip the next newline
                    if (receivedChar == '\r') {
                        skipNextNewline = true;
                    }

                    // Build up the line
                    String inputLine = inputBuffer.toString();

                    // Clear the buffer
                    inputBuffer.setLength(0);

                    // If the command is not empty, add it to the history
                    if (!inputLine.isEmpty()) {
                        commandHistory.add(inputLine);
                        historyIndex = commandHistory.size();
                    }

                    // Show that the session is still active
                    session.ping();

                    // Handle the command request
                    CommandResponse response = 
                            session.getApp().handleCommand(
                                    TerminalType.ANSI, inputLine);

                    // Ignore null responses
                    if (response == null) {
                        // Output the next prompt
                        screen.writeUserPrompt(session);
                        continue;
                    }

                    // Is it time to leave?
                    if (response.getCode() == TerminalCode.EXIT_CLIENT) {
                        out.println(response.getText());
                        break;
                    }

                    // Forced session break
                    if (session.isTimeToStop()) {
                        break;
                    }

                    // Is it time to go down one app?
                    if (response.getCode() == TerminalCode.EXIT_APP
                            && session.getApp().appParent != null) {
                        //app = app.appParent;
                    }

                    // Is it time to change apps?
                    if (response.getCode() == TerminalCode.CHANGE_APP) {
                        //app = session.getApp();
                        //session.setApp(app);
                        if (session.getApp().appParent != null) {
                            out.println(session.getApp().getIntro());
                        }
                    }

                    // Output the reply
                    if (response.getText().length() > 0) {
                        // Output the message
                        out.println(response.getText());
                    }

                    // Output the next prompt
                    screen.writeUserPrompt(session);
                }

                // Close the session
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
