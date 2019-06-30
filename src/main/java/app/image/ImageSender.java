package app.image;

import app.controller.MainScreenController;
import app.utils.SegmentSpec;
import app.websocket.message.SegmentMessage;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;

import javax.websocket.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

@ClientEndpoint
public class ImageSender implements ImageHandler {

    public static long segLength = 500; //milisekunde
    private Session userSession = null;

    private long last;
    private boolean started = false;
    private boolean running;
    private int file;
    private ArrayList<BufferedImage> imageBuffer;
    private BlockingQueue<SegmentSpec> segmentBuffer;

    public ImageSender() throws IOException {
        this.running = true;
        this.file = 0;
        imageBuffer = new ArrayList<>();
    }

    public ImageSender(URI address) throws IOException, DeploymentException {
        this();
        System.out.println("Web socket on " + address.toString());
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, address);
    }

    public ImageSender(Session session) throws IOException {
        this();
        this.userSession = session;
    }

    @Override
    public boolean sendImage(BufferedImage img) {
        if (!started) {
            this.last = System.currentTimeMillis();
            started = true;
        }

        long current = System.currentTimeMillis();

        try {
            if (current - last >= segLength) {
                imageBuffer.add(img);
                System.out.println("Sending video frames " + this.imageBuffer.size());
                last = current;
                //Send the segment through WebSocket
                sendSegment(generateSegment());
                this.imageBuffer = new ArrayList<>();
            } else {
                //baferuj sliku
                imageBuffer.add(img);
            }
        } catch (NullPointerException | IOException e) {
            System.err.println("Image sender error ");
            e.printStackTrace();
            this.running = false;
        }

        return running;
    }

    private SegmentMessage generateSegment(){
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();

        try {
            AWTSequenceEncoder encoder = new AWTSequenceEncoder(channel,Rational.R(this.imageBuffer.size(),1));
            System.out.println("images "+this.imageBuffer.size());
            for (BufferedImage img:this.imageBuffer) {
                encoder.encodeImage(img);
            }
            encoder.finish();
            SegmentMessage msg = new SegmentMessage(this.imageBuffer.size(),(int)channel.size(),Base64.encodeBase64String(channel.getContents()));
            NIOUtils.closeQuietly(channel);
            return msg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void stop() {
        try {
            userSession.close();
            this.running = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendSegment(SegmentMessage videoSegment) throws IOException {
        Gson g = new Gson();
        String json = g.toJson(videoSegment);
        this.userSession.getBasicRemote().sendText(json);
    }

    @OnMessage
    public void onMessage(String incomingSegment) throws InterruptedException, IOException {
        System.out.println("Other segment");
        Gson g = new Gson();
        SegmentMessage msg = g.fromJson(incomingSegment, SegmentMessage.class);
        String path = "resources/segment" + file++ + ".mp4";
        Files.write(Paths.get(path), Base64.decodeBase64(msg.video));
        if (this.segmentBuffer != null){
            this.segmentBuffer.put(new SegmentSpec(msg.frames,path));
        }else{
            System.err.println("segmentBuffer null");
        }
    }


    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("opening websocket");
        this.userSession = userSession;
        this.segmentBuffer = MainScreenController.mainScreen.startReceiver();
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason      the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        this.userSession = null;
        this.running = false;
    }

    @OnError
    public void processError(Throwable t) {
        t.printStackTrace();
    }

    public Session getUserSession() {
        return userSession;
    }

    public void setUserSession(Session userSession) {
        this.userSession = userSession;
    }
}
