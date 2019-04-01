package app.controller;

import app.dto.azure.recive.group.GetPersonDto;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

public class FaceDialogController implements Initializable {


    public GetPersonDto personToParse;
    public Image imageWithFaces;
    public ImageView originalIMagePane;
    public VBox detectedFacesPane;
    public Button finishDetectButton;

    public void finishPartition(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
