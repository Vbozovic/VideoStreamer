package app.gui;

import app.dto.FaceDto;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PersonPanel extends JPanel {

    private BufferedImage face;
    private ImagePanel facePart;
    private JPanel infoPart;
    private FaceDto faceInfo;

    public PersonPanel(ImagePanel imgPan,FaceDto faceDto){
        this();
        this.facePart = imgPan;
        this.faceInfo = faceDto;
        setup();
    }



    public PersonPanel() {
        super();
        face = null;
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        this.setLayout(new GridLayout(1,2));
        infoPart = new JPanel();
    }



    void setup(){
        facePart.setBorder(new TitledBorder("Person"));
        infoPart.setBorder(new TitledBorder("Face info"));

        infoPart.setLayout(new GridLayout(3,1));

        infoPart.add(new JLabel("Age: "+this.faceInfo.getAge()));
        infoPart.add(new JLabel("Gender: "+this.faceInfo.getGender()));
        infoPart.add(new JLabel("Gender confidence: "+this.faceInfo.getConfidence()));

        this.add(infoPart);
        this.add(facePart);
    }

}
