package net.aufdemrand.simplejetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;



public class Main {

    public static void main(String[] args) throws Exception {

        //
        // Create a server object.
        //

        Server server = new Server(1234);

        // Specify the Session ID Manager
        HashSessionIdManager id_manager = new HashSessionIdManager();
        server.setSessionIdManager(id_manager);
        ContextHandler context = new ContextHandler("/");
        server.setHandler(context);
        HashSessionManager manager = new HashSessionManager();
        SessionHandler sessions = new SessionHandler(manager);
        context.setHandler(sessions);

        // Hello, World!
        HelloWorld hello = new HelloWorld();
        sessions.setHandler(hello);

        // Start the server!
        server.start();
        server.join();

    }

}
