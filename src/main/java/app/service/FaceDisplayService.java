package app.service;

import org.opencv.core.Rect;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class FaceDisplayService {

    private JPanel displayTo;

    public FaceDisplayService(JPanel displayTo) {
        this.displayTo = displayTo;
    }

    public void addFaces(BufferedImage image, Rect[] faces){

        //Add face Panel to displayTo

    }


}
