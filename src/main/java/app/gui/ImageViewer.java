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
import java.util.ArrayList;
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
        this.setSize(img.getWidth()*2,img.getHeight()+50);
    }



    private void setup(){
        JPanel migPanel = new JPanel(new GridLayout(1,2));
        this.webcamPan = new ImagePanel((int)(this.getWidth()/2),(int)(this.getHeight()/2),0,0);
        this.facePan = new JPanel();
        migPanel.add(webcamPan);
        migPanel.add(facePan);
        faceDisplay = new FaceDisplayService(facePan);

        this.setContentPane(migPanel);
        //this.pack();

        this.setSize(600,400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.contacts = new ContactBook(Config.getInstance().contact_book);
        try {
            this.contacts.load();
            System.out.println(this.contacts);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public ContactBook getContacts() {
        return contacts;
    }

    public void setContacts(ContactBook contacts) {
        this.contacts = contacts;
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
