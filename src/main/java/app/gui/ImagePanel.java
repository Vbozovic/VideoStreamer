package app.gui;

import javafx.scene.layout.Border;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    public BufferedImage img;
    private int width;
    private int height;

    public ImagePanel(int width,int height) {
        super();
        img = null;
        this.width = width;
        this.height = height;
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    }

    public ImagePanel(BufferedImage img){
        this(img.getWidth(),img.getHeight());
        this.img = img;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (img != null){
            g.drawImage(img,10,15,this.width,this.height-15,this);
        }else{
            super.paintComponent(g);
        }
    }

}
