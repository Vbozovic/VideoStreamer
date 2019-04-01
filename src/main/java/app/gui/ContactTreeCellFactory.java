package app.gui;

import app.Utils;
import app.controller.FaceDialogController;
import app.dto.azure.recive.group.GetPersonDto;
import app.dto.azure.recive.group.PersistedFaceDto;
import app.error_handling.AzureException;
import app.service.AzureService;
import app.service.Config;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class ContactTreeCellFactory extends TreeCell<GetPersonDto> {

    private ContextMenu nodeMenu;

    public ContactTreeCellFactory(){
        nodeMenu = new ContextMenu();
        MenuItem addMenuItem = new MenuItem("Add face");
        MenuItem deleteMenuItem = new MenuItem("Delete contact");
        nodeMenu.getItems().addAll(addMenuItem,deleteMenuItem);
        addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage)getTreeView().getScene().getWindow();
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("JPG","*.jpg"),
                        new FileChooser.ExtensionFilter("PNG","*.png")
                );
                File f = fc.showOpenDialog(stage);
                if(f == null || f.isDirectory()){
                    return;
                }
                System.out.println(f.getAbsolutePath());
                try {

                    GetPersonDto person = getTreeView().getSelectionModel().getSelectedItems().get(0).getValue();

                    Utils.loadAndWaitWindow("src/main/resources/AddFaceDialog.fxml",500,350,(FaceDialogController controller)->{
                        controller.personToParse = person;
                        try {
                            BufferedImage bufferedImage;
                            controller.imageWithFaces = SwingFXUtils.toFXImage(Utils.getBufferedImage(f.getAbsolutePath()), null);
                            controller.originalIMagePane.setImage(controller.imageWithFaces);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });

                    //PersistedFaceDto face = AzureService.addFaceToPerson(image,person.personId,Config.getInstance().group_id);
                    //person.persistedFaceIds.add(face.persistedFaceId); //Add persisted face id to faceDto

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GetPersonDto person = getTreeView().getSelectionModel().getSelectedItems().get(0).getValue();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete contact");
                alert.setHeaderText("You are about to delete a contact");
                alert.setContentText("Are you ok with this?");

                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    try {
                        AzureService.deletePerson(person.personId, Config.getInstance().group_id);
                        TreeItem<GetPersonDto> personNode = getTreeView().getSelectionModel().getSelectedItems().get(0);
                        personNode.getParent().getChildren().remove(personNode);
                        getTreeView().refresh();
                    } catch (AzureException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    public ContactTreeCellFactory(ContextMenu nodeMenu) {
        this.nodeMenu = nodeMenu;
    }


    @Override
    protected void updateItem(GetPersonDto item, boolean empty) {
        super.updateItem(item,empty);
        if (!empty){
            setText(item.toString());

            setContextMenu(nodeMenu);
        }
    }



}
