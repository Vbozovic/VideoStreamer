package app.threads;

import app.image.ImageHandler;
import app.image.SeekableInMemoryByteChannel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;

public class VideoReciverTask implements Runnable {


    private boolean running;
    private ObjectInputStream in;
    private ImageHandler display;

    public VideoReciverTask(ObjectInputStream in, ImageHandler display) {
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
                int length = this.in.readInt();
                int frames = this.in.readInt();
                System.out.println("Received video frames: "+frames+" length "+length);
                byte[] video = new byte[length];
                in.readFully(video);


                FrameGrab fg = FrameGrab.createFrameGrab(new SeekableInMemoryByteChannel(video));
                long timeout = (long) (1000 / frames);
                long last = System.currentTimeMillis();

                while (true){
                    if (last - System.currentTimeMillis() >= timeout) {
                        //display picutre
                        Picture pic = fg.getNativeFrame();
                        if (pic == null){
                            break;
                        }
                        System.out.print(".");
                        BufferedImage frame = AWTUtil.toBufferedImage(pic);
                        this.display.sendImage(frame);
                        last = System.currentTimeMillis();
                    }else{
                        Thread.sleep(0);
                    }
                }

            }
            in.close();
        } catch (IOException | JCodecException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        this.running = false;
    }
}
