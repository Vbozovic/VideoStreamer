package app.utils;

import app.controller.WebcamDisplayDontroller;
import app.image.ImageDisplayer;
import app.threads.WebcamScanner;
import com.github.sarxos.webcam.Webcam;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

import java.util.concurrent.Executors;

public class LoadControllers {

    public static void setupWebcamDisplay(WebcamDisplayDontroller controller) {
        controller.executor = Executors.newSingleThreadExecutor();
        Webcam cam = Webcam.getDefault();
        WebcamScanner sc = new WebcamScanner(new ImageDisplayer(controller.webcamView), cam);
        controller.executor.submit(sc);
        controller.webcamView.getScene().getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //kada zatvarmao prozor zubijamo i tred pul
                controller.executor.shutdown();
                sc.stop();
            }
        });
    }

}
