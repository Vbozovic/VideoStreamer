package app.dto.facex;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Contact implements Serializable {

    public VectorDto vector;
    public String name;
    @JsonIgnore
    public BufferedImage faceImage;
    public int width;
    public int height;

    public Contact(VectorDto vector, String name) {
        this.vector = vector;
        this.name = name;
    }

    public Contact(VectorDto vector, String name, BufferedImage faceImage, int width, int height) {
        this.vector = vector;
        this.name = name;
        this.faceImage = faceImage;
        this.width = width;
        this.height = height;
    }

    public Contact(VectorDto vector, String name, BufferedImage faceImage){
        this.vector = vector;
        this.name = name;
        this.faceImage = faceImage;
        this.width = faceImage.getWidth();
        this.height = faceImage.getHeight();
    }

    public Contact(){
        this.vector = new VectorDto();
        this.name = "";
    }

}
