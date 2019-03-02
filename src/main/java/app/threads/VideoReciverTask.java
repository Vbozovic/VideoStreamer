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
import org.opencv.core.*;
import java.awt.image.*;
import java.io.IOException;
import java.io.ObjectInputStream;

public class VideoReciverTask implements Runnable {


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
}
