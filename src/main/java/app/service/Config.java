package app.service;

import app.controller.ConfigController;
import app.utils.Utils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class Config{

    private static Config instance = null;

    public String group_id;

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
        } catch (IOException e) {
            e.printStackTrace();
            //Nema fajla
            try {
                Files.createFile(Paths.get("resources/config.properties"));
                String userGroup = "";
                userGroup = Utils.getWindowResult("src/main/resources/GroupDialog.fxml",(ConfigController cont)-> cont.result);
                Files.write(Paths.get("resources/config.properties"),("group_id = "+userGroup).getBytes(),StandardOpenOption.APPEND);
                this.group_id = userGroup;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
