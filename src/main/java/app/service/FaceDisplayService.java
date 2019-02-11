package app.service;

import app.dto.FaceDto;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class FaceDisplayService {

    private JPanel displayTo;

    public FaceDisplayService(JPanel displayTo) {
        this.displayTo = displayTo;
    }

    public void addFaces(BufferedImage image, List<FaceDto> faces){

        //Add face Panel to displayTo

    }


}
