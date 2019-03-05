package app.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ContactBook {

    private String path;
    ArrayList<Contact> contacts;


    public ContactBook() {
        contacts = new ArrayList<>();
    }

    public ContactBook(String path) throws IOException {
        this();
        this.path = path;
        load();
    }


    private void load() throws IOException {
        //load from a file
        File f = new File(this.path);
        String json = FileUtils.readFileToString(f,"utf-8");
        ObjectMapper om = new ObjectMapper();
        contacts = om.readValue(json,contacts.getClass());
    }

    public void save() throws IOException {
        //save to a file
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(this.contacts);
        File f = new File(this.path);
        FileUtils.write(f,json,"utf-8",false);
    }

    public boolean addContact(Contact contact) {
        return contacts.add(contact);
    }



}
