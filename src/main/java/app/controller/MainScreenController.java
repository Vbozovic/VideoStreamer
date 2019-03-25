package app.controller;

import app.dto.azure.recive.group.GetGroupDto;
import app.dto.azure.recive.group.GetPersonDto;
import app.error_handling.AzureException;
import app.error_handling.CreateGroupException;
import app.error_handling.GetGroupException;
import app.error_handling.ListPersonsException;
import app.model.MainScreenModel;
import app.service.AzureService;
import app.service.Config;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    private MainScreenModel model;


    public SplitPane beginScreen;
    public ListView<GetPersonDto> contactList;


    public void displayAdContact(ActionEvent actionEvent) {
    }

    public void displayAddFace(ActionEvent actionEvent) {
    }

    public void displayTrainingStatus(ActionEvent actionEvent) {
    }

    public void train(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            this.model = new MainScreenModel();
        }catch (GetGroupException gge){
            if(gge.statusCode == 404){
                //create new group
                try {
                    AzureService.createGroup("Vuk contacts","Custom data",Config.getInstance().group_id);
                } catch (CreateGroupException e1) {
                    e1.printStackTrace();
                    System.exit(-1);
                }
            }
        } catch (AzureException le){
            le.printStackTrace();
        }


    }
}
