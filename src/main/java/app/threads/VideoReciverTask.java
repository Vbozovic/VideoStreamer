package app.threads;

import app.gui.Displayer;
import app.gui.ImageViewer;
import app.server.Server;
import com.github.sarxos.webcam.WebcamPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class VideoReciverTask implements Runnable, WindowListener {


    private boolean runnig;
    private Server server;
    private ObjectInputStream in;


    public VideoReciverTask(Server server, ObjectInputStream in) {
        this.server = server;
        this.in = in;
        runnig = true;
    }

    public void run() {


        ImageViewer iv = new ImageViewer();
        iv.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        iv.setSize(660,340);
        iv.addWindowListener(this);
        iv.setVisible(true);


        try {

            int colorModel;
            int width,height;

            height = in.readInt();
            width = in.readInt();

            byte[] pixels = new byte[height*width*3];// 3 = broj bajtova po pikselu
            while (runnig){
                //System.out.println("Read");
                in.readFully(pixels);
                iv.displayImage(createImageFromBytes(pixels,width,height));
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage createImageFromBytes(byte[] pixels,int width,int height) {
        BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
        img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(pixels, pixels.length), new Point() ) );
        return img;
    }

    public void stop(){
        this.runnig = false;
    }

    public void windowOpened(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {

    }

    public void windowClosed(WindowEvent e) {
        //System.out.println("Closed");
        this.stop();
    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {

    }
}
