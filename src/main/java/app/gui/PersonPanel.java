package app.gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PersonPanel extends JPanel {

    private BufferedImage face;
    private ImagePanel facePart;
    private JPanel infoPart;


    public PersonPanel(ImagePanel imgPan){
        this();
        facePart = imgPan;
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
        this.add(infoPart);
        this.add(facePart);
    }

}
