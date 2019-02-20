package app.threads;

import app.Utils;
import app.callback.FaceAttributeCallback;
import app.client.FaceClient;
import app.gui.ImageViewer;
import app.service.FaceDetectService;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import javax.imageio.ImageIO;
import javax.rmi.CORBA.Util;
import javax.swing.*;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class VideoReciverTask implements Runnable, WindowListener {


    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private boolean runnig;
    private ObjectInputStream in;
    private FaceDetectService detect;

    public VideoReciverTask(ObjectInputStream in) {
        this();
        this.in = in;
    }

    public VideoReciverTask() {
        this.runnig = true;
        this.detect = new FaceDetectService();
    }


    public void run() {


        try {

            int colorModel;
            int width, height;

            height = in.readInt();
            width = in.readInt();

            byte[] pixels = new byte[height * width * 3];// 3 = broj bajtova po pikselu
            int skipCounter = 0;
            while (runnig) {
                //System.out.println("Read");
                in.readFully(pixels);
                BufferedImage img = Utils.createImageFromBytes(pixels, width, height);

                if(skipCounter++ >= 10){
                    skipCounter = 0;
                    detect.getIv().displayWebcamImage(detect.faceStuff(img));
                }else{
                    detect.getIv().displayWebcamImage(img);
                }
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
