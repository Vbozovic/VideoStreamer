package app.websocket;

import org.glassfish.tyrus.server.Server;

import javax.websocket.DeploymentException;

public class SegmentServer {
    private static final String SERVER_HOSTNAME = "localhost";
    private static final int SERVER_PORT = 8025;
    private static final String SERVER_CONTEXT_PATH = "/websocket";

    private Server server;

    public static final String SERVER_ADDRESS =
            "ws://" + SERVER_HOSTNAME + ":" + SERVER_PORT + SERVER_CONTEXT_PATH;


    public void start() throws DeploymentException {
        try {
            System.out.println("Starting server for " + SERVER_ADDRESS);

            server = new Server(
                    SERVER_HOSTNAME,
                    SERVER_PORT,
                    SERVER_CONTEXT_PATH,
                    null,
                    SegmentEndpoint.class
            );

            server.start();
        } catch (DeploymentException e) {
            server = null;
            throw e;
        }
    }
    public void stop() {
        if (server != null) {
            System.out.println("Stopping server for " + SERVER_ADDRESS);

            server.stop();
        }
    }


}
