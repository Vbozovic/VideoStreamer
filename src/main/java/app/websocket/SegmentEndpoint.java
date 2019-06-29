package app.websocket;

import app.controller.MainScreenController;
import app.image.ImageSender;
import app.model.MainScreenModel;
import app.threads.WebcamScanner;
import app.websocket.message.SegmentMessage;
import com.github.sarxos.webcam.Webcam;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;

import javax.websocket.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/video")
public class SegmentEndpoint {

    private Session session = null;
    private static int file = 0;
    private BlockingQueue<SegmentMessage> send;

    public SegmentEndpoint(){
        System.out.println("SegmentEndpoint started");
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println("Connection opened "+session.getRequestURI());
        this.session = session;
        //open a ws connection towards the sender
        System.out.println("Starting ");
        WebcamScanner sc = new WebcamScanner(new ImageSender(this.session), Webcam.getDefault());
        MainScreenController.pool.submit(sc);
        MainScreenController.mainScreen.scanner = sc;
        this.send = MainScreenController.mainScreen.startReceiver();
    }

    @OnMessage
    public void onMessage(String segmentMessage,Session session) throws IOException, InterruptedException {
        Gson g = new Gson();
        SegmentMessage msg = g.fromJson(segmentMessage,SegmentMessage.class);
        System.out.println("Got segment");
        if(send != null){
            send.put(msg);
        }else{
            System.err.println("Msg null");
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("Connection closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        System.out.println("Error decoding ");
        throwable.printStackTrace();
    }


}

