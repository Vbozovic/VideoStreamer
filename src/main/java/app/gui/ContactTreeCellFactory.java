package app.gui;

import app.controller.FaceDialogController;
import app.controller.WebcamDisplayDontroller;
import app.dto.azure.recive.group.GetPersonDto;
import app.error_handling.AzureException;
import app.image.ImageDisplayer;
import app.service.AzureService;
import app.service.Config;
import app.threads.WebcamScanner;
import app.utils.Utils;
import com.github.sarxos.webcam.Webcam;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Executors;

public class ContactTreeCellFactory extends TreeCell<GetPersonDto> {

    private ContextMenu nodeMenu;

    public ContactTreeCellFactory(){
        nodeMenu = new ContextMenu();
        MenuItem addFaceMenuItem = new MenuItem("Add face from picture");
        MenuItem deleteMenuItem = new MenuItem("Delete contact");
        MenuItem addFaceFromCameraMenuItem = new MenuItem("Add face from camera");
        nodeMenu.getItems().addAll(addFaceMenuItem,deleteMenuItem,addFaceFromCameraMenuItem);

        addFaceMenuItem.setOnAction(event -> {
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
                        controller.scan();//detect faces
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

                //PersistedFaceDto face = AzureService.addFaceToPerson(image,person.personId,Config.getInstance().group_id);
                //person.persistedFaceIds.add(face.persistedFaceId); //Add persisted face id to faceDto

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        deleteMenuItem.setOnAction(event -> {
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
        });

        addFaceFromCameraMenuItem.setOnAction(event -> {
            try {
                Utils.loadAndWaitWindow("src/main/resources/WebcamDisplay.fxml",500,400,(WebcamDisplayDontroller controller)->{
                    controller.person = getTreeView().getSelectionModel().getSelectedItems().get(0).getValue();
                    controller.executor = Executors.newSingleThreadExecutor();
                    Webcam cam = Webcam.getDefault();
                    WebcamScanner sc = new WebcamScanner(new ImageDisplayer(controller.webcamView),cam);
                    controller.executor.submit(sc);
                    controller.webcamView.getScene().getWindow().setOnCloseRequest(event1 -> {
                        //kada zatvarmao prozor zubijamo i tred pul
                        controller.executor.shutdown();
                        sc.stop();
                    });
                });
            } catch (IOException e) {
                e.printStackTrace();
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
