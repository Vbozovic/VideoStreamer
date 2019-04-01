package app.controller;

import app.dto.azure.recive.group.GetPersonDto;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class FaceDisplayController {

    public ImageView smalFacePane;
    public GetPersonDto person;

    public void selectFace(ActionEvent actionEvent) {
        System.out.println(person);
        Stage st = (Stage) smalFacePane.getScene().getWindow();
        st.close();
    }
}
