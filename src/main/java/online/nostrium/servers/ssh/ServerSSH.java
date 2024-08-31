/*
 * SSH server running usually on a custom port
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.servers.ssh;

import online.nostrium.servers.Server;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.CommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.shell.InteractiveProcessShellFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Collections;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.sftp.server.SftpSubsystemFactory;

public class ServerSSH extends Server {

    private SshServer sshd;

    @Override
    public String getId() {
        return "Server_SSH";
    }

    @Override
    public int getPort() {
        return 7070;
    }

    @Override
    protected void boot() {
        sshd = SshServer.setUpDefaultServer();
        sshd.setPort(getPort());

        // Set the key pair provider (usually RSA keys)
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get("hostkey.ser")));

        // Set up password authenticator (in this case, allow all passwords for simplicity)
        sshd.setPasswordAuthenticator((username, password, session) -> true);

        // Set up a command factory to handle commands - this is where you'll integrate the telnet command handler logic
        sshd.setCommandFactory(new CommandFactory() {
            @Override
            public Command createCommand(ChannelSession channel, String command) {
                // Here, you need to create a Command object that behaves like your Telnet ClientHandler
                return new SSHCommand(command);
            }
        });

        // Set up the shell factory - can be customized or replaced with something more advanced
        sshd.setShellFactory(InteractiveProcessShellFactory.INSTANCE);

        // Optionally add SFTP support
        sshd.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));

        try {
            sshd.start();
            isRunning = true;
            System.out.println("SSH Server started on port: " + getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void shutdown() {
        if (sshd != null && isRunning) {
            try {
                sshd.stop();
                isRunning = false;
                System.out.println("SSH Server shut down successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ServerSSH server = new ServerSSH();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        server.boot();
    }

    // This class would need to map the SSH commands to your existing Telnet command handling
    private static class SSHCommand implements Command {

        private final String command;
        private InputStream in;
        private OutputStream out;
        private OutputStream err;
        private ExitCallback exitCallback;

        public SSHCommand(String command) {
            this.command = command;
        }

        @Override
        public void start(ChannelSession channelSession, Environment env) throws IOException {
            // Here, translate the SSH command to something your existing command handler can process
            out.write(("Executed command: " + command + "\n").getBytes());
            out.flush();
            exitCallback.onExit(0); // Indicate that the command has completed successfully
        }

        @Override
        public void destroy(ChannelSession channelSession) throws Exception {
            // Clean up if necessary
        }

        @Override
        public void setInputStream(InputStream inputStream) {
            this.in = inputStream;
        }

        @Override
        public void setOutputStream(OutputStream outputStream) {
            this.out = outputStream;
        }

        @Override
        public void setErrorStream(OutputStream errStream) {
            this.err = errStream;
        }

        @Override
        public void setExitCallback(ExitCallback callback) {
            this.exitCallback = callback;
        }
    }
}
