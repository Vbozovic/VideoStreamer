package app.threads;

import app.image.SocketByteChannelReader;
import app.utils.Utils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class VideoReciverTask implements Runnable {


    private boolean running;
    private SocketByteChannelReader in;
    private ImageView display;

    public VideoReciverTask(ObjectInputStream in, ImageView display) {
        this();
        this.in = new SocketByteChannelReader(new DataInputStream(in));
        this.display = display;
    }

    public VideoReciverTask() {
        this.running = true;
    }


    public void run() {
        try {
            FrameGrab grab = FrameGrab.createFrameGrab(this.in);
            Picture pic;
            while (running) {
                //System.out.println("Read");
                pic = grab.getNativeFrame();
                BufferedImage img = AWTUtil.toBufferedImage(pic);
                synchronized (this.display){
                    this.display.setImage(SwingFXUtils.toFXImage(img,null));
                }
            }
            in.close();
        } catch (IOException | JCodecException e) {
            e.printStackTrace();
        }

    }

    public void stop(){
        this.running = false;
    }
}
