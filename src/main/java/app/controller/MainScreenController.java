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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

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
            URL url = new File("src/main/resources/AddContactWindow.fxml").toURL();
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load(url);
            Stage stage = new Stage();
            stage.setScene(new Scene(root,400,300));

            AddContactController controller = (AddContactController)loader.getController();


            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            this.model = new MainScreenModel(this.contactTree);
            this.model.fillTree();
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
