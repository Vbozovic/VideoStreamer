package app.dto;

import java.awt.image.BufferedImage;

public class Contact {

    public VectorDto vector;
    public String name;

    public Contact(VectorDto vector, String name) {
        this.vector = vector;
        this.name = name;
    }
}
