package app.threads;

import app.server.Server;
import com.github.sarxos.webcam.Webcam;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SenderTask implements Runnable {

    private Socket client;
    private boolean running;
    private Server server;

    public SenderTask(Socket client) {
        this.client = client;
        running = false;
    }

    public void run() {


        try {
            Webcam cam = Webcam.getDefault();
            cam.open();
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());

            while(running){
                out.writeObject(cam.getImage());
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
