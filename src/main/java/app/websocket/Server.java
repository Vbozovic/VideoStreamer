package app.websocket;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/video",
        decoders = VideoSegnemtDecoder.class,
        encoders = VideoSegmentEncoder.class)
public class Server {

    private Session session;
    private static Set<Server> chatEndpoints = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(
            Session session,
            @PathParam("username") String username) throws IOException {
        System.out.println("Connection opened");
        this.session = session;
        chatEndpoints.add(this);

//        Message message = new Message();
//        message.setFrom(username);
//        message.setContent("Connected!");
//        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session)
            throws IOException {

//        message.setFrom(users.get(session.getId()));
//        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("Connection closed");
        chatEndpoints.remove(this);
//        Message message = new Message();
//        message.setFrom(users.get(session.getId()));
//        message.setContent("Disconnected!");
//        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }


}

