package app.threads;

import app.image.ImageHandler;
import app.image.SeekableInMemoryByteChannel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.api.specific.AVCMP4Adaptor;
import org.jcodec.common.SeekableDemuxerTrack;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;
import org.jcodec.scale.AWTUtil;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VideoReciverTask implements Runnable {


    private boolean running;
    private DataInputStream in;
    private ImageHandler display;

    public VideoReciverTask(DataInputStream in, ImageHandler display) {
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
                int read = in.read(video,0,length);
                System.out.println("Stream read "+read);

                for (int i = 0; i < 20; i++) {
                    System.out.print(video[i]);
                }

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
