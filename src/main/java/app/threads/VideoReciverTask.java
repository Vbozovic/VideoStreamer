package app.threads;

import app.utils.Utils;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class VideoReciverTask implements Runnable {


    private boolean running;
    private DataInputStream in;
    private ImageView display;

    public VideoReciverTask(ObjectInputStream in, ImageView display) {
        this();
        this.in = new DataInputStream(in);
        this.display = display;
    }

    public VideoReciverTask() {
        this.running = true;
    }


    public void run() {
        try {
            int width, height;

            String mdata = in.readUTF();
            String[] datas = mdata.split(",");

            height = Integer.parseInt(datas[0]);
            width = Integer.parseInt(datas[1]);
            System.out.println("recived meta data "+height+" "+width);

            byte[] pixels = new byte[height * width * 3];// 3 = broj bajtova po pikselu
            while (running) {
                //System.out.println("Read");
                in.readFully(pixels);
                try{
                    //BufferedImage img = Utils.createImageFromBytes(pixels, width, height);
                    ByteInputStream bin = new ByteInputStream(pixels,pixels.length);
                    BufferedImage img = ImageIO.read(bin);
                    if (img == null){
                        continue;
                    }
                    synchronized (this.display){
                        this.display.setImage(SwingFXUtils.toFXImage(img,null));
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stop(){
        this.running = false;
    }
}
