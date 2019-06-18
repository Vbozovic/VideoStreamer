package app.image;

import app.websocket.VideoSegmentEncoder;
import app.websocket.message.SegmentMessage;
import org.apache.commons.codec.binary.Base64;
import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Rational;

import javax.websocket.*;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@ClientEndpoint(encoders = VideoSegmentEncoder.class)
public class ImageSender implements ImageHandler {

    private static long segLength = 500; //milisekunde
    private Session userSession = null;

    private AWTSequenceEncoder encoder;
    private long time = 500;
    private long last;
    private int currentFrames = 0;
    private SeekableInMemoryByteChannel channel;
    private boolean started = false;

    public ImageSender(URI address) throws IOException, DeploymentException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this,address);
        this.channel = new SeekableInMemoryByteChannel();
        this.encoder = new AWTSequenceEncoder(this.channel, Rational.R(15, 1));
    }

    @Override
    public void sendImage(BufferedImage img) {
        if(!started){
            this.last = System.currentTimeMillis();
            started = true;
        }

        long current = System.currentTimeMillis();

        try{
            if (current - last >= time) {
                encoder.encodeImage(img);
                this.currentFrames++;
                encoder.finish();
                System.out.println("Sending video frames "+currentFrames);
                last = current;
                byte[] video = channel.getContents();

                //Send the segment through WebSocket
                sendSegment(new SegmentMessage(currentFrames,video.length,video));

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
        }catch(IOException e){
            System.err.println("Image sender error ");
            e.printStackTrace();
        }


    }

    @Override
    public void stop() {
        try {
            userSession.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendSegment(SegmentMessage videoSegment){
        try {
            this.userSession.getBasicRemote().sendObject(videoSegment);
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
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
     * @param reason the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        this.userSession = null;
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
