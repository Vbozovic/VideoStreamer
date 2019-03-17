package app.gui;

import app.Utils;
import app.dto.Contact;
import app.dto.FaceDto;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PersonPanel extends JPanel implements MouseListener {

    private BufferedImage face;
    private ImagePanel facePart;
    private JPanel infoPart;
    private FaceDto faceInfo;
    private String name;

    public PersonPanel(ImagePanel imgPan,FaceDto faceDto,String name){
        this(name);
        this.face = imgPan.img;
        this.facePart = imgPan;
        this.faceInfo = faceDto;
        setup();
    }



    public PersonPanel(String name) {
        super();
        this.name = name;
        System.out.println(name);
        face = null;
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        this.setLayout(new GridLayout(1,2));
        infoPart = new JPanel();
    }



    void setup(){
        this.addMouseListener(this);
        facePart.setBorder(new TitledBorder("Person"));
        infoPart.setBorder(new TitledBorder("Face info"));

        infoPart.setLayout(new GridLayout(3,1));

        infoPart.add(new JLabel("Age: "+this.faceInfo.getAge()));
        infoPart.add(new JLabel("Gender: "+this.faceInfo.getGender()));
        infoPart.add(new JLabel("Gender confidence: "+this.faceInfo.getConfidence()));

        this.add(infoPart);
        this.add(facePart);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2){
            String name = JOptionPane.showInputDialog("Unesite ime");
            ImageViewer.getInstance().getContacts().addContact(new Contact(faceInfo.getVector(),name, Utils.imgToBytes(face),face.getWidth(),face.getHeight()));
            try {
                ImageViewer.getInstance().getContacts().save();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
