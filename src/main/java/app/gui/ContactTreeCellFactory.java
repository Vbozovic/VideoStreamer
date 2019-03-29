package app.gui;

import app.dto.azure.recive.group.GetPersonDto;
import app.dto.azure.recive.group.PersistedFaceDto;
import app.error_handling.AzureException;
import app.service.AzureService;
import app.service.Config;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
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
                File f = new FileChooser().showOpenDialog(stage);
                System.out.println(f.getAbsolutePath());
                try {
                    BufferedImage image = ImageIO.read(f);
                    GetPersonDto person = getTreeView().getSelectionModel().getSelectedItems().get(0).getValue();

                    PersistedFaceDto face = AzureService.addFaceToPerson(image,person.personId,Config.getInstance().group_id);
                    person.persistedFaceIds.add(face.persistedFaceId); //Add persisted face id to faceDto

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (AzureException e) {
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
