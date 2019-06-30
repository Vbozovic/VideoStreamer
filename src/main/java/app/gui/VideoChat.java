package app.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.net.URL;


public class VideoChat extends Application{


    private Stage mainStage;
    private Scene mainScreenScene;


    public VideoChat(){
        super();
    }


    public static void main(String[] args){
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        System.out.println("Application Closed by click to Close Button(X)");
                        System.exit(0);
                    }
                });
            }
        });

        URL url = new File("src/main/resources/VideoStreamer.fxml").toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Hello World");
        this.mainScreenScene = new Scene(root,750,300);
        primaryStage.setScene(this.mainScreenScene);
        primaryStage.show();;

    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
