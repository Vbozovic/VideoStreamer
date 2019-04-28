package app.controller;

import app.client.AzureClient;
import app.dto.azure.recive.group.GetPersonDto;
import app.dto.azure.recive.group.TrainStatusDto;
import app.error_handling.AzureException;
import app.error_handling.CreateGroupException;
import app.error_handling.GetGroupException;
import app.gui.ContactTreeCellFactory;
import app.model.MainScreenModel;
import app.server.Server;
import app.service.AzureService;
import app.service.Config;
import app.threads.FaceIdentifierTask;
import app.threads.WebcamScanner;
import app.utils.Utils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.util.Callback;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainScreenController implements Initializable {

    public static String database = "resources/contacts.ser";
    public Button callButton;
    public TextField ipAddrField;
    public ImageView chatImageView;

    private MainScreenModel model;

    public SplitPane beginScreen;
    public TreeView<GetPersonDto> contactTree;

    private Server callServer = null;
    private ExecutorService pool;

    public void displayAdContact(ActionEvent actionEvent) {
        try {
            Utils.loadAndWaitWindow("src/main/resources/AddContactWindow.fxml", 400, 300, (AddContactController controller) -> {
                controller.model = this.model;
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayAddFace(ActionEvent actionEvent) {
        if (!this.contactTree.getSelectionModel().getSelectedItems().isEmpty()) {
            System.out.println(this.contactTree.getSelectionModel().getSelectedItems().get(0).getValue());
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Adding a face");
            alert.setContentText("You must have a contact selected in order to add a face");

            alert.showAndWait();
        }
    }

    public void displayTrainingStatus(ActionEvent actionEvent) {
        try {
            TrainStatusDto dto = AzureService.getGroupTrainStatus(Config.getInstance().group_id);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Training status");
            alert.setHeaderText("Training progress:");

            dto.createdDateTime = dto.createdDateTime.substring(0,dto.createdDateTime.lastIndexOf("."));
            alert.setContentText("Status: "+dto.status+"\nCreated: "+dto.createdDateTime);
            Optional<ButtonType> result = alert.showAndWait();
        } catch (AzureException e) {
            e.printStackTrace();
        }
    }

    public void train(ActionEvent actionEvent) {
        try {
            AzureService.trainGroup(Config.getInstance().group_id);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Training");
            alert.setHeaderText("The training has begun");
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date d = new Date();
            alert.setContentText(dateFormat.format(d));
            Optional<ButtonType> result = alert.showAndWait();

        } catch (AzureException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            this.pool = Executors.newCachedThreadPool();
            this.callServer = new Server(8080, this.pool, this.chatImageView);
            this.pool.execute(this.callServer); //pokrecemo jedno slusanje za konekciju
            this.model = new MainScreenModel(this.contactTree);
            this.contactTree.setCellFactory(param -> new ContactTreeCellFactory());
        } catch (GetGroupException gge) {
            if (gge.statusCode == 404) {
                //create new group
                try {
                    AzureService.createGroup("Vuk contacts", "Custom data", Config.getInstance().group_id);
                } catch (CreateGroupException e1) {
                    e1.printStackTrace();
                    System.exit(-1);
                }
            }
        } catch (AzureException le) {
            le.printStackTrace();
        }


    }

    public void initiateCall(ActionEvent actionEvent) {
        callServer.establish_async(this.ipAddrField.getText());
    }

    public void displayChatoptions(ContextMenuEvent contextMenuEvent) {
        //Desni klik na sliku
        ContextMenu menu = new ContextMenu();

        MenuItem endCall = new MenuItem("End call");
        MenuItem idFace = new MenuItem("Identify");

        menu.getItems().addAll(endCall, idFace);

        endCall.setOnAction(value -> {
            //Zavrsavanje poziva
            this.callServer.endCall();
        });

        idFace.setOnAction(value -> {
            //Identifikovanje lica
            BufferedImage img = this.callServer.getCamImg();
            Platform.runLater(new FaceIdentifierTask(this.model,img));
//            this.pool.execute(new FaceIdentifierTask(this.model,img));
        });

        menu.show(this.chatImageView, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
    }
}
