package app.service;

import app.Utils;
import app.callback.FaceAttributeCallback;
import app.client.FaceClient;
import app.gui.ImageViewer;
import okhttp3.OkHttpClient;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

public class FaceDetectService {

    private CascadeClassifier faceClas;
    private Rect[] faces;
    private OkHttpClient client;
    private ImageViewer iv;

    public FaceDetectService() {
        this.faceClas = new CascadeClassifier("C:\\Users\\ybv\\Desktop\\VideoStreamer\\resources\\haarcascade_frontalcatface.xml");
        this.faces = new Rect[0];
        this.client = new OkHttpClient();
        this.iv = ImageViewer.getInstance();
    }


    public BufferedImage faceStuff(BufferedImage img) {
        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        Mat mat_pixels = new Mat(img.getHeight(), img.getWidth(), CvType.CV_8UC3);
        mat_pixels.put(0, 0, pixels);

        faceDetect(mat_pixels, img);

        mat_pixels.get(0, 0, pixels);
        return Utils.createImageFromBytes(pixels, img.getWidth(), img.getHeight());
    }

    public void faceDetect(Mat img, BufferedImage image) {
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
            //System.out.println("Number of faces changed "+this.faces.length);
            byte[] bytes = Utils.imgToBytes(image);
            System.out.println("Sending FaceX");
            try {
                FaceClient.postFaceDetect(this.client, new FaceAttributeCallback(iv.getFaceDisplay(), Utils.deepCopy(image)), bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        for (int i = 0; i < facesArray.length; i++) {
            Imgproc.rectangle(img, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 2550), 3);
        }
    }

    public ImageViewer getIv() {
        return iv;
    }
}
