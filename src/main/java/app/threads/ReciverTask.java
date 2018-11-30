package app.threads;

import app.gui.Displayer;
import app.gui.ImageViewer;
import app.server.Server;
import com.github.sarxos.webcam.WebcamPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ReciverTask implements Runnable{

    private Socket client;
    private boolean runnig;
    private Server server;

    public ReciverTask(Socket client,Server server) {
        this.client = client;
        runnig = true;
        this.server = server;
    }

    public void run() {


        ImageViewer iv = new ImageViewer();
        iv.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        iv.setSize(660,340);
        iv.setVisible(true);


        try {

            int colorModel;

            ObjectInputStream in = new ObjectInputStream(client.getInputStream());

            int width,height;

            height = in.readInt();
            width = in.readInt();

            byte[] pixels = new byte[height*width*3];// 3 = broj bajtova po pikselu
            while (runnig){
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

}
