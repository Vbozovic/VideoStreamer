package app.controller;

import app.Utils;
import app.dto.azure.recive.group.GetGroupDto;
import app.dto.azure.recive.group.GetPersonDto;
import app.error_handling.AzureException;
import app.error_handling.CreateGroupException;
import app.error_handling.GetGroupException;
import app.error_handling.ListPersonsException;
import app.gui.ContactTreeCellFactory;
import app.model.MainScreenModel;
import app.service.AzureService;
import app.service.Config;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    private MainScreenModel model;

    public SplitPane beginScreen;
    public TreeView<GetPersonDto> contactTree;


    public void displayAdContact(ActionEvent actionEvent) {
        try {

            Utils.loadAndWaitWindow("src/main/resources/AddContactWindow.fxml",400,300,(AddContactController controller)->{
                controller.model = this.model;
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayAddFace(ActionEvent actionEvent) {
        if(!this.contactTree.getSelectionModel().getSelectedItems().isEmpty()){
            System.out.println(this.contactTree.getSelectionModel().getSelectedItems().get(0).getValue());
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Adding a face");
            alert.setContentText("You must have a contact selected in order to add a face");

            alert.showAndWait();
        }
    }

    public void displayTrainingStatus(ActionEvent actionEvent) {
    }

    public void train(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            this.model = new MainScreenModel(this.contactTree);
            this.contactTree.setCellFactory(new Callback<TreeView<GetPersonDto>, TreeCell<GetPersonDto>>() {
                @Override
                public TreeCell<GetPersonDto> call(TreeView<GetPersonDto> param) {
                    return new ContactTreeCellFactory();
                }
            });
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
