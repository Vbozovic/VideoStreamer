package app.threads;

import app.gui.ImageViewer;
import app.server.Server;
import com.github.sarxos.webcam.WebcamPanel;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ReciverTask implements Runnable {

    private Socket client;
    private boolean runnig;
    private Server server;

    public ReciverTask(Socket client,Server server) {
        this.client = client;
        runnig = false;
        this.server = server;
    }

    public void run() {


        ImageViewer iv = new ImageViewer();
        iv.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        iv.setSize(660,340);
        iv.setVisible(true);


        try {
            ObjectInputStream in = (ObjectInputStream) client.getInputStream();

            while (runnig){
                iv.displayImage((BufferedImage)in.readObject());
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void stop(){
        this.runnig = false;
    }

}
