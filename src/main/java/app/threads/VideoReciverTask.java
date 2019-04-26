package app.threads;

import app.utils.Utils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;

public class VideoReciverTask implements Runnable {


    private boolean runnig;
    private ObjectInputStream in;
    private ImageView display;

    public VideoReciverTask(ObjectInputStream in, ImageView display) {
        this();
        this.in = in;
        this.display = display;
    }

    public VideoReciverTask() {
        this.runnig = true;
    }


    public void run() {
        try {
            int width, height;

            height = in.readInt();
            width = in.readInt();

            System.out.println("recived meta data "+height+" "+width);

            byte[] pixels = new byte[height * width * 3];// 3 = broj bajtova po pikselu
            int skipCounter = 0;
            while (runnig) {
                //System.out.println("Read");
                in.readFully(pixels);
                BufferedImage img = Utils.createImageFromBytes(pixels, width, height);
                System.out.println("Image recived");
                synchronized (this.display){
                    this.display.setImage(SwingFXUtils.toFXImage(img,null));
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
