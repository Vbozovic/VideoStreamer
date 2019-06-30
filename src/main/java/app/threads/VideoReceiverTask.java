package app.threads;

import app.image.ImageHandler;
import app.image.ImageSender;
import app.image.SeekableInMemoryByteChannel;
import app.utils.SegmentSpec;
import app.websocket.message.SegmentMessage;
import org.apache.commons.codec.binary.Base64;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class VideoReceiverTask implements Runnable {


    private boolean running;
    private ImageHandler display;
    private BlockingQueue<SegmentSpec> in;

    public VideoReceiverTask(BlockingQueue<SegmentSpec> in, ImageHandler display) {
        this();
        this.in = in;
        this.display = display;
    }

    public VideoReceiverTask() {
        this.running = true;
    }


    public void run() {
        try {

            while (running) {
                if (this.in.isEmpty()){
                    continue;
                }
                ArrayList<SegmentSpec> segments = new ArrayList<>();

                this.in.drainTo(segments);
                SegmentSpec segment = segments.get(segments.size()-1);

                FileChannelWrapper wrap = NIOUtils.readableChannel(new File(segment.file));
                FrameGrab fg = FrameGrab.createFrameGrab(wrap);

                long timeout = (long) (ImageSender.segLength / segment.frames);
                long last = System.currentTimeMillis();

                while (true) {
                    if (System.currentTimeMillis() - last >= timeout) {
                        //display picutre
                        Picture pic = fg.getNativeFrame();
                        if (pic == null) {
                            break;
                        }
                        BufferedImage frame = AWTUtil.toBufferedImage(pic);
                        this.display.sendImage(frame);
                        last = System.currentTimeMillis();
                    } else {
                        Thread.sleep(0);
                    }
                }

                wrap.close();
                for (SegmentSpec seg: segments){
                    Files.delete(Paths.get(seg.file));
                }
            }
        } catch (IOException | JCodecException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Stop receiver");

    }

    public void stop() {
        this.running = false;
    }
}
