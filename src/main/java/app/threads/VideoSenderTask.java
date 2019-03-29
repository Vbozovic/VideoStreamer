package app.threads;

import com.github.sarxos.webcam.Webcam;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class VideoSenderTask implements Runnable{

    private Socket client;
    private boolean running;
    private ObjectOutputStream out;


    public VideoSenderTask(ObjectOutputStream out) {
        this.out = out;
        running = true;
    }

    public void run() {


        try {
            Webcam cam = Webcam.getDefault();
            cam.open();
            //ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());

            BufferedImage img = cam.getImage();


            out.writeInt(img.getHeight()); //saljemo visinu
            out.writeInt(img.getWidth()); //saljemo sirinu

            System.out.println(img.getHeight()+" "+img.getWidth());

            while(running){
                //System.out.println("Send");
                byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();

                out.write(pixels);
                out.flush();

                img = cam.getImage();
            }

            out.close();
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
