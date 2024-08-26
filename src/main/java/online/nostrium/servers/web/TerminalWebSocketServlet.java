package online.nostrium.servers.web;

import org.eclipse.jetty.websocket.server.JettyWebSocketServlet;
import org.eclipse.jetty.websocket.server.JettyWebSocketServletFactory;


public class TerminalWebSocketServlet extends JettyWebSocketServlet {

    @Override
    protected void configure(JettyWebSocketServletFactory factory) {
        System.out.println("Configuring WebSocket factory");
        factory.register(TerminalWebSocketHandler.class);
    }
    
}
