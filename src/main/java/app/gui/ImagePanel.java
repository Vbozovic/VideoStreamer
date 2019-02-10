package app.gui;

import javafx.scene.layout.Border;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    BufferedImage img;

    public ImagePanel(int width, int height) {
        super();
        img = null;
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (img != null){
            g.drawImage(img,0,0,img.getWidth(),img.getHeight(),this);
        }else{
            super.paintComponent(g);
        }
    }

}
