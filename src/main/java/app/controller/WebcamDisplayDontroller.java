package app.controller;

import app.dto.azure.recive.group.GetPersonDto;
import app.utils.Utils;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class WebcamDisplayDontroller{
    public ImageView webcamView;
    public Button takeWebcamPictureButton;
    public GetPersonDto person;

    public ExecutorService executor;

    public void takeWebcamPicture(ActionEvent actionEvent) {
        BufferedImage img = null;
        synchronized (webcamView){
            img = SwingFXUtils.fromFXImage(webcamView.getImage(),null);
        }

        final Image image = SwingFXUtils.toFXImage(img, null);

        try {
            Utils.loadAndWaitWindow("src/main/resources/AddFaceDialog.fxml",(FaceDialogController controller)->{
                controller.personToParse = person;
                BufferedImage bufferedImage;
                controller.imageWithFaces = image;
                controller.originalIMagePane.setImage(controller.imageWithFaces);
                controller.scan();//detect faces
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
