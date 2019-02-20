package app.service;

import app.dto.FaceDto;
import app.gui.ImagePanel;
import app.gui.PersonPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class FaceDisplayService {

    private JPanel displayTo;
    private LinkedList<PersonPanel> panels;

    public FaceDisplayService(JPanel displayTo) {
        this.displayTo = displayTo;
        this.panels = new LinkedList<>();
        displayTo.setLayout(new GridLayout(3,1));
    }

    public void setFaces(BufferedImage image, List<FaceDto> faces) {

        int counter = 0;
        //Add face Panel to displayTo
        for (PersonPanel pan : panels) {
            ImagePanel imPan = panelCrop(image,counter,faces);
            pan = new PersonPanel(imPan,faces.get(counter));
            pan.repaint();
            counter++;
        }

        for (; counter < faces.size(); counter++) {
            ImagePanel imPan = panelCrop(image,counter,faces);
            PersonPanel ppan = new PersonPanel(imPan,faces.get(counter));
            displayTo.add(ppan);
            ppan.repaint();
            imPan.repaint();
        }

        displayTo.repaint();
    }

    private ImagePanel panelCrop(BufferedImage image,int counter,List<FaceDto> faces){
        int UX,UY,LX,LY;
        UX = faces.get(counter).ULX();
        UY = faces.get(counter).ULY();
        LX = faces.get(counter).LRX();
        LY = faces.get(counter).LRY();

        ImagePanel imPan = new ImagePanel(image.getSubimage(UX,UY,LX-UX,LY-UY),10,10);
        return imPan;
    }

}
