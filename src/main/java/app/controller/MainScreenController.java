package app.controller;

import app.dto.azure.recive.group.GetPersonDto;
import app.dto.azure.recive.group.TrainStatusDto;
import app.error_handling.AzureException;
import app.error_handling.CreateGroupException;
import app.error_handling.GetGroupException;
import app.gui.ContactTreeCellFactory;
import app.image.ImageDisplayer;
import app.image.ImageSender;
import app.model.MainScreenModel;
import app.service.AzureService;
import app.service.Config;
import app.threads.FaceIdentifierTask;
import app.threads.VideoReciverTask;
import app.threads.WebcamScanner;
import app.utils.Utils;
import app.websocket.SegmentServer;
import app.websocket.message.SegmentMessage;
import com.github.sarxos.webcam.Webcam;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import jdk.nashorn.internal.ir.Block;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class MainScreenController implements Initializable {

    public Button callButton;
    public TextField ipAddrField;
    public MediaView chatImageView;

    private MainScreenModel model;
    public WebcamScanner scanner = null;
    public SplitPane beginScreen;
    public TreeView<GetPersonDto> contactTree;

    private SegmentServer segmentWebSocket;

    public static MainScreenController mainScreen = null;
    public static ExecutorService pool = Executors.newCachedThreadPool();

    public void displayAdContact(ActionEvent actionEvent) {
        try {
            Utils.loadAndWaitWindow("src/main/resources/AddContactWindow.fxml", 400, 300, (AddContactController controller) -> {
                controller.model = this.model;
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initMediPlayer(BlockingQueue<String> sources) throws InterruptedException {
        if(sources == null){
            return;
        }
        String src = sources.take();
        if (src == null){
            return;
        }
        MediaPlayer player = new MediaPlayer(new Media(src));
        player.setAutoPlay(true);

        player.setOnEndOfMedia(()->{
            try {
                initMediPlayer(sources);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        this.chatImageView.setMediaPlayer(player);
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
            mainScreen = this;
            this.model = new MainScreenModel(this.contactTree);
            this.contactTree.setCellFactory(param -> new ContactTreeCellFactory());
            this.segmentWebSocket = new SegmentServer();
            this.segmentWebSocket.start();
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
        } catch (AzureException | DeploymentException le) {
            le.printStackTrace();
        }


    }

    public void initiateCall(ActionEvent actionEvent) {
        String ip = this.ipAddrField.getText();
        try {
            this.scanner = new WebcamScanner(new ImageSender(new URI(String.format("ws://%s:8025/websocket/video",ip))), Webcam.getDefault());
            pool.submit(scanner);
        } catch (IOException | DeploymentException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void displayChatOptions(ContextMenuEvent contextMenuEvent) {
        //Desni klik na sliku
        ContextMenu menu = new ContextMenu();

        MenuItem endCall = new MenuItem("End call");
        MenuItem idFace = new MenuItem("Identify");

        menu.getItems().addAll(endCall, idFace);

        endCall.setOnAction(value -> {
            //Zavrsavanje poziva
            if(this.scanner != null){
                this.scanner.stop();
                this.scanner = null;
            }else{
                System.err.println("Scanner not set");
            }
        });

        idFace.setOnAction(value -> {
            //Identifikovanje lica
//            BufferedImage img = this.callServer.getCamImg();
            Platform.runLater(new FaceIdentifierTask(this.model,null));
//            this.pool.execute(new FaceIdentifierTask(this.model,img));
        });

        this.beginScreen.setContextMenu(menu);
    }

    public BlockingQueue<String> startReceiver() throws InterruptedException {
        BlockingQueue<String> toReturn = new LinkedBlockingQueue<>();
        initMediPlayer(toReturn);
        return toReturn;
    }

}
