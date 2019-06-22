package app.image;

import app.websocket.message.SegmentMessage;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Rational;

import javax.websocket.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class ImageSender implements ImageHandler {

    private static long segLength = 500; //milisekunde
    private Session userSession = null;

    private AWTSequenceEncoder encoder;
    private long time = 500;
    private long last;
    private int currentFrames = 0;
    private SeekableInMemoryByteChannel channel;
    private boolean started = false;
    private boolean running;

    public ImageSender() throws IOException {
        this.channel = new SeekableInMemoryByteChannel();
        this.encoder = new AWTSequenceEncoder(this.channel, Rational.R(15, 1));
        this.running = true;
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
            if (current - last >= time) {
                encoder.encodeImage(img);
                this.currentFrames++;
                encoder.finish();
                System.out.println("Sending video frames " + currentFrames);
                last = current;
                byte[] video = channel.getContents();

                //Send the segment through WebSocket
                sendSegment(new SegmentMessage(currentFrames, video.length, Base64.encodeBase64String(video)));

                NIOUtils.closeQuietly(this.channel);
                this.channel = new SeekableInMemoryByteChannel();
                this.encoder = new AWTSequenceEncoder(this.channel, Rational.R((int) currentFrames, 1));

                this.currentFrames = 0; //reset broj frejmova
            } else {
                //baferuj sliku
                System.out.print("!");
                this.currentFrames++;
                encoder.encodeImage(img);
            }
        } catch (IOException e) {
            System.err.println("Image sender error ");
            e.printStackTrace();
            this.running = false;
        }

        return running;
    }

    @Override
    public void stop() {
        try {
            userSession.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendSegment(SegmentMessage videoSegment) throws IOException {
        Gson g = new Gson();
        String json = g.toJson(videoSegment);
        this.userSession.getBasicRemote().sendText(json);
    }

    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("opening websocket");
        this.userSession = userSession;
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
