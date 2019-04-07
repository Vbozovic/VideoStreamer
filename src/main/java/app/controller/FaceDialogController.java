package app.controller;

import app.utils.Utils;
import app.dto.azure.recive.detect.FaceDetectDto;
import app.dto.azure.recive.group.GetPersonDto;
import app.error_handling.AzureException;
import app.service.AzureService;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class FaceDialogController implements Initializable {


    public GetPersonDto personToParse;
    public Image imageWithFaces;
    public ImageView originalIMagePane;
    public VBox detectedFacesPane;
    public Button finishDetectButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void scan() {
        try {
            FaceDetectDto[] faces = AzureService.detectFaces(SwingFXUtils.fromFXImage(imageWithFaces, null));

            Arrays.asList(faces).forEach((face) -> {
                try {
                    AnchorPane facePane = Utils.loadComponent("src/main/resources/FaceDisplay.fxml", (FaceDisplayController controller) -> {
                        BufferedImage midImg = Utils.cropImageFaces(face, SwingFXUtils.fromFXImage(imageWithFaces, null));
                        Image img = SwingFXUtils.toFXImage(midImg, null);
                        controller.smalFacePane.setImage(img);
                        controller.person = this.personToParse;
                    });
                    detectedFacesPane.getChildren().add(facePane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (AzureException e) {
            e.printStackTrace();
        }

    }
}
