package app.threads;

import app.gui.Displayer;
import app.gui.ImageViewer;
import app.server.Server;
import com.github.sarxos.webcam.WebcamPanel;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Point;
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
    private CascadeClassifier faceClas;



    public VideoReciverTask(Server server, ObjectInputStream in) {
        this();
        this.server = server;
        this.in = in;
    }

    public VideoReciverTask() {
        this.runnig = true;
        this.faceClas = new CascadeClassifier("C:\\Users\\Vuk\\Desktop\\Diplomskiv2\\resources\\haarcascade_frontalcatface.xml");
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

    private void faceDetect(Mat img){
        Mat grayScale = new Mat(img.rows(),img.cols(),img.type());
        Imgproc.cvtColor(img,grayScale,Imgproc.COLOR_BayerBG2BGR);
        Imgproc.equalizeHist(grayScale,grayScale);
        int faceSize = 0;
        if(Math.round(img.rows()*0.2f) > 0){
            faceSize = Math.round(img.rows()*0.2f);
        }

        MatOfRect faces = new MatOfRect();
        faceClas.detectMultiScale(grayScale,faces,1.1,2,0| Objdetect.CASCADE_SCALE_IMAGE,new Size(faceSize,faceSize));
        Rect[] facesArray = faces.toArray();
        for(int i=0;i<facesArray.length;i++){
            Imgproc.rectangle(img,facesArray[i].tl(),facesArray[i].br(),new Scalar(0,255,0,2550),3);
        }
    }


    private BufferedImage faceStuff(BufferedImage img) {
        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        Mat mat_pixels = new Mat(img.getHeight(),img.getWidth(), CvType.CV_8UC3);
        mat_pixels.put(0,0,pixels);

        //transformation
        faceDetect(mat_pixels);

        mat_pixels.get(0,0,pixels);
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
