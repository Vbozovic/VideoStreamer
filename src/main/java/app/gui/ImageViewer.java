package app.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageViewer extends JFrame {

    private JLabel lab;

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
        this.pack();
    }

}
