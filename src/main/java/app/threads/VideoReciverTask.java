package app.threads;

import app.utils.Utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;

public class VideoReciverTask implements Runnable {



    private boolean runnig;
    private ObjectInputStream in;

    public VideoReciverTask(ObjectInputStream in) {
        this();
        this.in = in;
    }

    public VideoReciverTask() {
        this.runnig = true;
    }


    public void run() {
        try {
            int width, height;

            height = in.readInt();
            width = in.readInt();


            byte[] pixels = new byte[height * width * 3];// 3 = broj bajtova po pikselu
            int skipCounter = 0;
            while (runnig) {
                //System.out.println("Read");
                in.readFully(pixels);
                BufferedImage img = Utils.createImageFromBytes(pixels, width, height);

                if(skipCounter++ >= 10){
                    skipCounter = 0;
                    //detect.getIv().displayWebcamImage(detect.faceStuff(img));
                }else{
                    //detect.getIv().displayWebcamImage(img);
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
