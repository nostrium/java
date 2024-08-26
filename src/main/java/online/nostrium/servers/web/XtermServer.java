package online.nostrium.servers.web;

import java.io.File;
import online.nostrium.main.Folder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class XtermServer {    
    
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

//        // Setup the context handler for the web application
//        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        context.setContextPath("/");
        
         // Setup the context handler for the web application
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");

        // Set the resource base to the ./run/www directory
        File folder = Folder.getFolderWWW();
        context.setResourceBase(folder.getAbsolutePath());

        // Add servlet to serve static files
        ServletHolder staticFileServlet = new ServletHolder("default", DefaultServlet.class);
        staticFileServlet.setInitParameter("dirAllowed", "true");
        context.addServlet(staticFileServlet, "/");

        
        
        // Add WebSocket servlet for terminal connections
        context.addServlet(TerminalWebSocketServlet.class, "/terminal");

        
        
        // Start the server
        server.setHandler(context);
        System.out.println("Server started on port 8080");
        server.start();
        server.join();
    }
}
