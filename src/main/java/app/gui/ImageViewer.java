package app.gui;

import app.dto.ContactBook;
import app.service.Config;
import app.service.FaceDisplayService;
import org.opencv.core.Core;
import org.opencv.core.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ImageViewer extends JFrame {

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static ImageViewer instance = null;

    private JPanel facePan;
    private ImagePanel webcamPan;
    private FaceDisplayService faceDisplay;
    private ContactBook contacts;

    public static ImageViewer getInstance(){

        if(instance == null){
            instance = new ImageViewer();
        }
        return instance;
    }

    private ImageViewer(){
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

        try {
            this.contacts = new ContactBook(Config.getInstance().contact_book);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public FaceDisplayService getFaceDisplay() {
        return faceDisplay;
    }

    public void setFaceDisplay(FaceDisplayService faceDisplay) {
        this.faceDisplay = faceDisplay;
    }

    public JPanel getFacePan() {
        return facePan;
    }
}
