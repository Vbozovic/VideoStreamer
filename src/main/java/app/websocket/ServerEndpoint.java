package app.websocket;

import app.websocket.message.SegmentMessage;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@javax.websocket.server.ServerEndpoint(value = "/video",
        decoders = VideoSegnemtDecoder.class,
        encoders = VideoSegmentEncoder.class)
public class ServerEndpoint {

    private Session session = null;
    private static Set<ServerEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();

    public ServerEndpoint(){

        System.out.println("ServerEndpoint started");

    }

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
    public void onMessage(Session session, SegmentMessage msg)
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

