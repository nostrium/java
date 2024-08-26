package online.nostrium.servers.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.server.JettyWebSocketServletFactory;
import org.eclipse.jetty.websocket.server.JettyWebSocketServlet;
import java.util.logging.Logger;

public class JettySocketExample {

    private static final Logger logger = Logger.getLogger(JettySocketExample.class.getName());

    @WebSocket
    public static class TerminalWebSocketHandler {

        @OnWebSocketConnect
        public void onConnect(Session session) {
            try {
                session.getRemote().sendString("Connected to WebSocket server.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @OnWebSocketMessage
        public void onMessage(Session session, String message) {
            try {
                session.getRemote().sendString("Echo: " + message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class TerminalWebSocketServlet extends JettyWebSocketServlet {
        @Override
        protected void configure(JettyWebSocketServletFactory factory) {
            factory.setCreator((req, res) -> new TerminalWebSocketHandler());
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ServletHolder wsHolder = new ServletHolder("ws-events", TerminalWebSocketServlet.class);
        context.addServlet(wsHolder, "/terminal/*");

        logger.info("Starting WebSocket server on port 8080...");
        server.start();
        logger.info("Server started successfully.");
        server.join();
    }
}
