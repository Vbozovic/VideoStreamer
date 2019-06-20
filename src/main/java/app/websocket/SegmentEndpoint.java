package app.websocket;

import app.websocket.message.SegmentMessage;

import javax.websocket.*;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/video",
        decoders = {VideoSegnemtDecoder.class},
        encoders = {VideoSegmentEncoder.class})
public class SegmentEndpoint {

    private Session session = null;

    public SegmentEndpoint(){
        System.out.println("SegmentEndpoint started");
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println("Connection opened "+session.getRequestURI());
        this.session = session;

    }

    @OnMessage
    public void onMessage(SegmentMessage msg,Session session){
        System.out.println("Got segment "+msg.length);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("Connection closed");
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

