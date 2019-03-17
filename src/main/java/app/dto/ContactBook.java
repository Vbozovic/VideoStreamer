package app.dto;

import app.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import com.fasterxml.jackson.core.type.TypeReference;


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
        //load from a file
        FileInputStream fin = new FileInputStream(this.path);
        ObjectInputStream oin = new ObjectInputStream(fin);
        try {
            this.contacts = (ArrayList<Contact>) oin.readObject();
            oin.close();
            fin.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            this.contacts = new ArrayList<>();
        }
    }

    public void save() throws IOException {
        //save to a file
        FileOutputStream fsout = new FileOutputStream(this.path);
        ObjectOutputStream os = new ObjectOutputStream(fsout);
        os.writeObject(this.contacts);
        os.close();
        fsout.close();
    }

    public boolean addContact(Contact contact) {
        return contacts.add(contact);
    }

    public Contact findContact(FaceDto face){

        for (int i = 0; i < this.contacts.size() ; i++) {
            Contact c = this.contacts.get(i);
            FaceDto mockFace = new FaceDto();
            mockFace.setVector(c.vector);
            mockFace.setFaceImage(Utils.createImageFromBytes(c.faceImage,c.width,c.height));
            if(mockFace.compareApi(face)){
                return c;
            }
        }
        return new Contact(); //returning empty contact
    }

    @Override
    public String toString() {
        return "ContactBook{" +
                "path='" + path + '\'' +
                ", contacts=" + contacts +
                '}';
    }
}
