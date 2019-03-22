package app.dto;

import app.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import com.fasterxml.jackson.core.type.TypeReference;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class ContactBook implements Serializable{

    private String path;
    ArrayList<Contact> contacts;


    public ContactBook() {
        contacts = new ArrayList<>();
    }

    public ContactBook(String path) {
        this();
        this.path = path;
    }


    public void load() throws IOException {
        ObjectMapper om = new ObjectMapper();
        File input = new File(this.path);
        this.contacts = om.readValue(input,new TypeReference<ArrayList<Contact>>(){});
        for(Contact c: this.contacts){
            File f = new File(this.path+"images/"+c.name+".jpg");
            c.faceImage = ImageIO.read(f);
        }
    }

    public void save() throws IOException {
        File f = new File(this.path);
        FileOutputStream fout = new FileOutputStream(f);
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(this.contacts);
        fout.write(json.getBytes());
        fout.close();
        for(Contact c : this.contacts){
            File output = new File(this.path+"images/"+c.name+".jpg");
            ImageIO.write(c.faceImage,"jpg",output);
        }
    }

    public boolean addContact(Contact contact) {
        return contacts.add(contact);
    }

    public Contact findContact(FaceDto face){

        for (int i = 0; i < this.contacts.size() ; i++) {
            Contact c = this.contacts.get(i);
            FaceDto mockFace = new FaceDto();
            mockFace.setVector(c.vector);
            mockFace.setFaceImage(c.faceImage);
            if(mockFace.compareApi(face)){
                return c;
            }
        }
        System.out.println("No group found");
        return new Contact(face.getVector(),"",face.getFaceImage()); //returning empty contact
    }

    @Override
    public String toString() {
        return "ContactBook{" +
                "path='" + path + '\'' +
                ", contacts=" + contacts +
                '}';
    }
}
