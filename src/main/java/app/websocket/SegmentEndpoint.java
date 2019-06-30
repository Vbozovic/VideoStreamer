package app.websocket;

import app.controller.MainScreenController;
import app.image.ImageSender;
import app.model.MainScreenModel;
import app.threads.WebcamScanner;
import app.utils.SegmentSpec;
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

    private BlockingQueue<SegmentSpec> msgBuffer;
    private Session session = null;
    private int file;

    public SegmentEndpoint() {
        System.out.println("SegmentEndpoint started");
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println("Connection opened " + session.getRequestURI());
        this.session = session;
        this.file = 0;
        //open a ws connection towards the sender
        System.out.println("Starting ");
        //Start image sending
        MainScreenController.mainScreen.startScanner(new WebcamScanner(new ImageSender(this.session), Webcam.getDefault()));
        this.msgBuffer = MainScreenController.mainScreen.startReceiver();
    }

    @OnMessage
    public void onMessage(String segmentMessage, Session session) throws IOException, InterruptedException {
        Gson g = new Gson();
        SegmentMessage msg = g.fromJson(segmentMessage, SegmentMessage.class);
        System.out.println("Got segment");
        String path = "resources/segment" + file++ + ".mp4";
        Files.write(Paths.get(path), Base64.decodeBase64(msg.video));
        if (this.msgBuffer != null) {
            this.msgBuffer.put(new SegmentSpec(msg.frames, path));
        } else {
            System.err.println("msgBuffer null");
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("Connection closed");
        MainScreenController.mainScreen.stopChat();
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        System.out.println("Error decoding ");
        throwable.printStackTrace();
    }


}

