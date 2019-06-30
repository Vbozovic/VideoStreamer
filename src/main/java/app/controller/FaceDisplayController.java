package app.controller;

import app.dto.azure.recive.group.GetPersonDto;
import app.dto.azure.recive.group.PersistedFaceDto;
import app.error_handling.AzureException;
import app.service.AzureService;
import app.service.Config;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


//Panel sa kropovanim licima
public class FaceDisplayController {

    public ImageView smalFacePane;
    public GetPersonDto person;

    public void selectFace(ActionEvent actionEvent) {
        System.out.println(person);
        Stage st = (Stage) smalFacePane.getScene().getWindow();
        try {
            BufferedImage face = SwingFXUtils.fromFXImage(smalFacePane.getImage(),null);
            PersistedFaceDto dto = AzureService.addFaceToPerson(face,person.personId, Config.getInstance().group_id);
            //ImageIO.write(face,"png",new File(Config.getInstance().faces_folder+"\\"+dto.persistedFaceId+".png"));
        } catch (AzureException e) {
            e.printStackTrace();
        }
        st.close();
    }
}
