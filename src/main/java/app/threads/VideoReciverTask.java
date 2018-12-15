package app.threads;

import app.gui.Displayer;
import app.gui.ImageViewer;
import app.server.Server;
import com.github.sarxos.webcam.WebcamPanel;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

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


    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

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
        iv.setSize(660, 340);
        iv.addWindowListener(this);
        iv.setVisible(true);


        try {

            int colorModel;
            int width, height;

            height = in.readInt();
            width = in.readInt();

            byte[] pixels = new byte[height * width * 3];// 3 = broj bajtova po pikselu
            while (runnig) {
                //System.out.println("Read");
                in.readFully(pixels);
                BufferedImage img = createImageFromBytes(pixels, width, height);
                iv.displayImage(faceStuff(img));
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage faceStuff(BufferedImage img) {
        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        Mat mat = new Mat(img.getHeight(),img.getWidth(), CvType.CV_8UC3);
        mat.put(0,0,pixels);

        //transformation
        mat.get(0,0,pixels);
        return createImageFromBytes(pixels,img.getWidth(),img.getHeight());
    }

    private BufferedImage createImageFromBytes(byte[] pixels, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(pixels, pixels.length), new Point()));
        return img;
    }

    public void stop() {
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
