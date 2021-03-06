package app.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config{

    private static Config instance = null;

    public String group_id;
    public String faces_folder;

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
            group_id = p.getProperty("group_id");
            faces_folder = p.getProperty("faces_folder");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
