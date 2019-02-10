package app.threads;

import app.callback.FaceAttributeCallback;
import app.client.FaceClient;
import app.gui.ImageViewer;
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
import javax.swing.*;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class VideoReciverTask implements Runnable, WindowListener, Callback {


    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private boolean runnig;
    private ObjectInputStream in;
    private CascadeClassifier faceClas;
    private Rect[] faces;
    private ImageViewer iv;
    private OkHttpClient client;


    public VideoReciverTask(ObjectInputStream in) {
        this();
        this.in = in;
    }

    public VideoReciverTask() {
        this.client = new OkHttpClient();
        this.runnig = true;
        this.faceClas = new CascadeClassifier("C:\\Users\\ybv\\Desktop\\VideoStreamer\\resources\\haarcascade_frontalcatface.xml");
        this.faces = new Rect[0];

        DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
                .setConnectTimeout(5000);
        setupView();
    }

    private void setupView(){
        iv = new ImageViewer();
        iv.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        iv.setSize(660, 340);
        iv.addWindowListener(this);
        iv.setVisible(true);
    }

    public void run() {


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
                iv.displayWebcamImage(faceStuff(img));
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void faceDetect(Mat img,BufferedImage image) {
        Mat grayScale = new Mat(img.rows(), img.cols(), img.type());
        Imgproc.cvtColor(img, grayScale, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(grayScale, grayScale);
        int faceSize = 0;
        if (Math.round(img.rows() * 0.2f) > 0) {
            faceSize = Math.round(img.rows() * 0.2f);
        }


        MatOfRect faces = new MatOfRect();
        faceClas.detectMultiScale(grayScale, faces, 1.095, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(faceSize, faceSize));

        Rect[] facesArray; //detected faces
        facesArray = faces.toArray();

        if (this.faces.length != facesArray.length && facesArray.length != 0) {
            this.faces = facesArray;
            // new faces detected
            System.out.println("Number of faces changed "+this.faces.length);
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", baos);
                byte[] bytes = baos.toByteArray();
                System.out.println("Sending FaceX");
                FaceClient.postFaceDetect(this.client,new FaceAttributeCallback(),bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        for (int i = 0; i < facesArray.length; i++) {
            Imgproc.rectangle(img, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 2550), 3);
        }
    }


    private BufferedImage faceStuff(BufferedImage img) {
        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        Mat mat_pixels = new Mat(img.getHeight(), img.getWidth(), CvType.CV_8UC3);
        mat_pixels.put(0, 0, pixels);

        //transformation
        faceDetect(mat_pixels,img);

        mat_pixels.get(0, 0, pixels);
        return createImageFromBytes(pixels, img.getWidth(), img.getHeight());
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

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

    }
}
