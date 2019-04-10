package app.threads;

import app.image.ImageDisplayer;
import app.image.ImageHandler;
import com.github.sarxos.webcam.Webcam;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WebcamScanner implements Runnable{

    private boolean running;
    private ImageHandler out;
    private Webcam cam;

    public WebcamScanner(ImageDisplayer out,Webcam cam) {
        this.out = out;
        running = true;
        this.cam = cam;
    }

    public void run() {
        cam.open();
        BufferedImage img = cam.getImage();
        //out.writeInt(img.getHeight()); //saljemo visinu
        //out.writeInt(img.getWidth()); //saljemo sirinu

        while(running){
            out.sendImage(img);
            img = cam.getImage();
        }
        cam.close();
    }

    public void stop(){
        this.running = false;
    }


}
