package app.threads;

import app.image.ImageDisplayer;
import app.image.ImageHandler;
import com.github.sarxos.webcam.Webcam;

import java.awt.image.BufferedImage;

public class WebcamScanner implements Runnable{

    private boolean running;
    private ImageHandler out;
    private Webcam cam;

    public WebcamScanner(ImageHandler out,Webcam cam) {
        this.out = out;
        running = true;
        this.cam = cam;
    }

    public void run() {
        cam.open();
        BufferedImage img = cam.getImage();

        while(running){
            synchronized (cam){
                try {
                    out.sendImage(img);
                } catch (Exception e) {
                    e.printStackTrace();
                    this.stop();
                }
            }
            img = cam.getImage();
        }
        System.out.println("Scanner stopped");
        cam.close();
    }

    public void stop(){
        this.running = false;
    }


}
