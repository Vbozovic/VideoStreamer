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

    private Socket client;
    private boolean running;
    private ImageHandler out;
    private Webcam cam;

    public WebcamScanner(ImageDisplayer out,Webcam cam) {
        this.out = out;
        running = true;
        this.cam = cam;
    }

    public void run() {


        try {

            cam.open();

            BufferedImage img = cam.getImage();
            //out.writeInt(img.getHeight()); //saljemo visinu
            //out.writeInt(img.getWidth()); //saljemo sirinu

            while(running){
                out.sendImage(img);
                img = cam.getImage();
            }
            cam.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void stop(){
        this.running = false;
    }


}
