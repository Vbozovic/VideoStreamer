package app.controller;

import app.dto.azure.recive.group.CreatedPersonDto;
import app.dto.azure.recive.group.GetPersonDto;
import app.error_handling.AzureException;
import app.model.MainScreenModel;
import app.service.AzureService;
import app.service.Config;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddContactController implements Initializable {
    public Button addContactButton;
    public Button cancelButton;
    public TextField contactNameField;
    public TextField userDataField;

    @FXML
    public MainScreenModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void addContact(ActionEvent actionEvent) {
        Stage stage = (Stage) addContactButton.getScene().getWindow();
        try {
            CreatedPersonDto result = AzureService.createPerson(contactNameField.getText(), userDataField.getText(), Config.getInstance().group_id);
            GetPersonDto person = AzureService.getPerson(result.personId,Config.getInstance().group_id);
            model.addPerson(person);
        } catch (AzureException e) {
            e.printStackTrace();
        }
        stage.close();
    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
