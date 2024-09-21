/*
 * FTP server
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.ftp;

import com.guichaguri.minimalftp.FTPConnection;
import com.guichaguri.minimalftp.FTPServer;
import com.guichaguri.minimalftp.api.IFTPListener;
import com.guichaguri.minimalftp.api.IFileSystem;
import com.guichaguri.minimalftp.api.IUserAuthenticator;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.nostrium.logs.Log;
import online.nostrium.main.core;
import online.nostrium.servers.Server;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;


/**
 * FTP Server for Nostrium
 *
 * Author: Brito Date: 2024-09-20 Location: Germany
 */
public class ServerFTP extends Server {

    private FTPServer server;

    @Override
    public String getId() {
        return "Server_FTP";
    }

    @Override
    public int getPort() {
        if (core.config.debug) {
            return core.config.portFTP_Debug;
        } else {
            return core.config.portFTP;
        }
    }

    @Override
    public int getPortSecure() {
        if (core.config.debug) {
            return core.config.portFTPS_Debug;
        } else {
            return core.config.portFTPS;
        }
    }

    @Override
    protected void boot() {
        
        
        // Create the FTP server
        server = new FTPServer();

        // Create our custom authenticator
        //UserbaseAuthenticator auth = new UserbaseAuthenticator();
        IUserAuthenticator auth;
        auth = new IUserAuthenticator() {
            @Override
            public boolean needsUsername(FTPConnection ftpc) {
                return true;
            }

            @Override
            public boolean needsPassword(FTPConnection ftpc, String string, InetAddress ia) {
                return true;
            }

            @Override
            public IFileSystem authenticate(FTPConnection ftpc, InetAddress ia, String username, String password) throws IUserAuthenticator.AuthException {
                if(username == null || username.isEmpty()){
                    return null;
                }
                if(password == null || password.isEmpty()){
                    return null;
                }
                
                // get the user
                User user = UserUtils.login(username, password);
                if(user == null){
                    return null;
                }
                File folder = user.getFolder(true);
                // Creates a native file system
                return new NativeFileSystemModded(folder, user);
            }
        };
        server.setAuthenticator(auth);

        // Set our custom authenticator
        server.setAuthenticator(auth);

        // Register an instance of this class as a listener
        server.addListener(new CustomServer());

        // Changes the timeout to 10 minutes
        server.setTimeout(10 * 60 * 1000); // 10 minutes

        // Changes the buffer size
        server.setBufferSize(1024 * 5); // 5 kilobytes
        try {
            // Start it synchronously in our localhost and in the port
            server.listenSync(InetAddress.getByName("localhost"), this.getPort());
        } catch (IOException ex) {
            Logger.getLogger(ServerFTP.class.getName()).log(Level.SEVERE, null, ex);
        }

        isRunning = true;
    }

    @Override
    protected void shutdown() {
        try {
            server.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerFTP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Main method to run the FTP server in standalone mode.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        core.startConfig();

        ServerFTP ftpServer = new ServerFTP();
        ftpServer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ftpServer.stop();
        }));
    }

    private static class CustomServer implements IFTPListener {

        public CustomServer() {
        }

        @Override
        public void onConnected(FTPConnection ftpc) {
           Log.write("FTP", TerminalCode.LOGIN, "Connected", ftpc.getUsername());
        }

        @Override
        public void onDisconnected(FTPConnection ftpc) {
            Log.write("FTP", TerminalCode.LOGOUT, "Diconnected", ftpc.getUsername());
        }
    }
}
