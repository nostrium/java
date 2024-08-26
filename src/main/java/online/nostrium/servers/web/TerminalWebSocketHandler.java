package online.nostrium.servers.web;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@WebSocket
public class TerminalWebSocketHandler {

    private static ConcurrentMap<Session, Process> sessions = new ConcurrentHashMap<>();

    @OnWebSocketConnect
    @SuppressWarnings("CallToPrintStackTrace")
    public void onConnect(Session session) throws IOException {
        // Start a terminal process when a new WebSocket connection is made
        ProcessBuilder builder = new ProcessBuilder("bash"); // or "cmd" for Windows, "sh" for other shells
        Process process = builder.start();
        sessions.put(session, process);

        // Start a thread to read output from the terminal process
        new Thread(() -> {
            try (var inputStream = process.getInputStream()) {
                int read;
                byte[] buffer = new byte[1024];
                while ((read = inputStream.read(buffer)) != -1) {
                    session.getRemote().sendString(new String(buffer, 0, read));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @OnWebSocketMessage
    @SuppressWarnings("CallToPrintStackTrace")
    public void onMessage(Session session, String message) {
        Process process = sessions.get(session);
        if (process != null) {
            try {
                process.getOutputStream().write(message.getBytes());
                process.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        Process process = sessions.remove(session);
        if (process != null) {
            process.destroy();
        }
    }
}
