package app.dto;

import java.io.Serializable;

public class Contact implements Serializable {

    public VectorDto vector;
    public String name;
    public byte[] faceImage;
    public int width;
    public int height;

    public Contact(VectorDto vector, String name) {
        this.vector = vector;
        this.name = name;
    }

    public Contact(VectorDto vector, String name, byte[] faceImage, int width, int height) {
        this.vector = vector;
        this.name = name;
        this.faceImage = faceImage;
        this.width = width;
        this.height = height;
    }

    public Contact(VectorDto vector, String name, byte[] faceImage) {
        this.vector = vector;
        this.name = name;
        this.faceImage = faceImage;
    }

    public Contact(){
        this.vector = new VectorDto();
        this.name = "";
    }

}
