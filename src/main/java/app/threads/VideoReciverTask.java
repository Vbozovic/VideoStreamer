package app.threads;

import app.image.ImageHandler;
import app.image.SeekableInMemoryByteChannel;
import app.websocket.message.SegmentMessage;
import org.apache.commons.codec.binary.Base64;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class VideoReciverTask implements Runnable {


    private boolean running;
    private ImageHandler display;
    private BlockingQueue<SegmentMessage> in;

    public VideoReciverTask(BlockingQueue<SegmentMessage> in, ImageHandler display) {
        this();
        this.in = in;
        this.display = display;
    }

    public VideoReciverTask() {
        this.running = true;
    }


    public void run() {
        try {

            while (running) {
                SegmentMessage msg = this.in.take();
                System.out.println("Taken message");
                byte[] video = Base64.decodeBase64(msg.video);
                FrameGrab fg = FrameGrab.createFrameGrab(new SeekableInMemoryByteChannel(video));
                long timeout = (long) (1000 / msg.frames);
                long last = System.currentTimeMillis();

                while (true) {
                    if (last - System.currentTimeMillis() >= timeout) {
                        //display picutre
                        Picture pic = fg.getNativeFrame();
                        if (pic == null) {
                            break;
                        }
                        System.out.print(".");
                        BufferedImage frame = AWTUtil.toBufferedImage(pic);
                        this.display.sendImage(frame);
                        last = System.currentTimeMillis();
                    } else {
                        Thread.sleep(0);
                    }
                }

            }
        } catch (IOException | JCodecException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        this.running = false;
    }
}
