package app.gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    public BufferedImage img;
    private int width;
    private int height;
    private int offsetWidth;
    private int offsetHeight;

    public ImagePanel(int width,int height,int offsetWidth,int offsetHeight) {
        super();
        img = null;
        this.width = width;
        this.height = height;
        this.offsetWidth = offsetWidth;
        this.offsetHeight = offsetHeight;
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    }

    public ImagePanel(BufferedImage img,int offsetWidth, int offsetHeight){
        this(img.getWidth(),img.getHeight(),offsetWidth,offsetHeight);
        this.img = img;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (img != null){
            g.drawImage(img,this.offsetWidth,this.offsetHeight,this.width,this.height,this);
        }else{
            super.paintComponent(g);
        }
    }

}
