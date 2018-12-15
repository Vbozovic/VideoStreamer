package app.gui;

import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageViewer extends JFrame {


    private class ImagePanel extends JPanel{

        BufferedImage img;

        public ImagePanel() {
            super();
            img = null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(img,0,0,null);
            //super.paintComponent(g);
        }
    }

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private ImagePanel pan;
    private CascadeClassifier faceDetector;
    private Imgproc imgproc;

    public ImageViewer(){
        super();
        pan = new ImagePanel();
        setup();
    }

    public void displayImage(BufferedImage img){
        pan.img = img;
        pan.repaint();
    }

    private void setup(){
        this.getContentPane().setLayout(new FlowLayout());
        this.setContentPane(pan);


        faceDetector = new CascadeClassifier("resources/haarcascade_frontalcatface.xml");
        imgproc = new Imgproc();


        this.pack();
    }

}
