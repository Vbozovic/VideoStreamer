package app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConfigController {

    public String result;

    @FXML
    private TextField idField;

    @FXML
    public void submitId(){
        String res = idField.getText();
        if(res==null || res.length() == 0){
            Alert.AlertType alertAlertType;
            Alert alert =  new Alert(AlertType.ERROR);
            alert.setContentText("Id must be longer than 0");
            alert.setTitle("Group id format error");
            alert.showAndWait();
        }

        result = res;
        Stage stg = (Stage) idField.getScene().getWindow();
        stg.close();
    }

}
