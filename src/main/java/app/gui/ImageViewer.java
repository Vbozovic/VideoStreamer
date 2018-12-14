package app.gui;

import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageViewer extends JFrame {

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private JLabel lab;
    private CascadeClassifier faceDetector;
    private Imgproc imgproc;

    public ImageViewer(){
        super();
        lab = new JLabel();
        setup();
    }

    public void displayImage(BufferedImage img){


        lab.setIcon(new ImageIcon(img));
    }

    private void setup(){
        this.getContentPane().setLayout(new FlowLayout());
        this.getContentPane().add(lab);
        lab.setVisible(true);

        faceDetector = new CascadeClassifier("resources/haarcascade_frontalcatface.xml");
        imgproc = new Imgproc();


        this.pack();
    }

}
