package app.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config{

    private static Config instance = null;

    public String contact_book;


    public static Config getInstance(){
        if(instance == null){
            instance = new Config();
        }
        return instance;
    }

    private Config(){

        Properties p = new Properties();
        try {
            p.load( new FileReader("resources/config.properties"));
            contact_book = p.getProperty("contact_book");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
