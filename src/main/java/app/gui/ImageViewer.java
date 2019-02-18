package app.gui;

import app.service.FaceDisplayService;
import org.opencv.core.Core;
import org.opencv.core.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageViewer extends JFrame {

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private JPanel facePan;
    private ImagePanel webcamPan;
    private FaceDisplayService faceDisplay;


    public ImageViewer(){
        super();
        setup();
    }

    public void displayWebcamImage(BufferedImage img){
        webcamPan.img = img;
        webcamPan.repaint();
    }



    private void setup(){
        this.setSize(600,600);
        JPanel migPanel = new JPanel(new GridLayout(1,2));
        this.webcamPan = new ImagePanel((int)(this.getWidth()/1.5),(int)(this.getHeight()/1.5),0,0);
        this.facePan = new JPanel();
        migPanel.add(webcamPan);
        migPanel.add(facePan);
        faceDisplay = new FaceDisplayService(facePan);

        this.setContentPane(migPanel);
        this.pack();
    }

    public FaceDisplayService getFaceDisplay() {
        return faceDisplay;
    }

    public void setFaceDisplay(FaceDisplayService faceDisplay) {
        this.faceDisplay = faceDisplay;
    }
}
