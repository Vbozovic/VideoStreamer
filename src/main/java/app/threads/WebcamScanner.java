package app.threads;

import app.image.ImageDisplayer;
import app.image.ImageHandler;
import com.github.sarxos.webcam.Webcam;

import java.awt.image.BufferedImage;

public class WebcamScanner implements Runnable{

    private boolean running;
    private ImageHandler out;
    private Webcam cam;

    public void run() {

        BufferedImage img = cam.getImage();

        while(running){
            synchronized (cam){
                try {
                    if (img!=null && !out.sendImage(img)){
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    this.stop();
                }
            }
            img = cam.getImage();
        }
        cam.close();
        out.stop();
    }
    public WebcamScanner(ImageHandler out,Webcam cam) {
        this.out = out;
        running = true;
        this.cam = cam;
        this.cam.open();
    }
    public void stop(){
        this.running = false;
    }


}
