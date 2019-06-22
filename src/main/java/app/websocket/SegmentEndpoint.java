package app.websocket;

import app.websocket.message.SegmentMessage;
import com.google.gson.Gson;

import javax.websocket.*;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/video")
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
    public void onMessage(String segmentMessage,Session session){
        Gson g = new Gson();
        SegmentMessage msg = g.fromJson(segmentMessage,SegmentMessage.class);
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
        System.out.println("Error decoding ");
        throwable.printStackTrace();
    }


}

