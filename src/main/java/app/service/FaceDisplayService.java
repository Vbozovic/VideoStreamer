package app.service;

import app.Utils;
import app.dto.FaceDto;
import app.gui.ImagePanel;
import app.gui.ImageViewer;
import app.gui.PersonPanel;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FaceDisplayService {

    private JPanel displayTo;
    private ArrayList<FaceDto> presentFaces;

    public FaceDisplayService(JPanel displayTo) {
        this.displayTo = displayTo;
        this.presentFaces = new ArrayList<>();
        displayTo.setLayout(new GridLayout(3,1));
    }

    public void setFaces(BufferedImage image, List<FaceDto> faces) {
        displayTo.removeAll(); //remove all components
        for (int counter = 0; counter < faces.size(); counter++) {
            ImagePanel imPan = panelCrop(image,faces.get(counter));
            PersonPanel ppan = new PersonPanel(imPan,faces.get(counter));
            displayTo.add(ppan);
            ppan.repaint();
            imPan.repaint();
        }
        displayTo.repaint();
        ImageViewer.getInstance().repaint();
    }

    public void addFace(BufferedImage image, FaceDto face){

        boolean present = false;
        for (FaceDto pface: presentFaces){
            if(pface.getVector().equals(face.getVector())){
                present = true;
                break;
            }
        }
        if(!present){
            ImagePanel imPan = panelCrop(image,face);
            PersonPanel ppan = new PersonPanel(imPan,face);
            displayTo.add(ppan);
        }
    }

    private ImagePanel panelCrop(BufferedImage image,FaceDto face){
        ImagePanel imPan = new ImagePanel(Utils.cropImageFaces(face,image),10,10);
        return imPan;
    }

}
