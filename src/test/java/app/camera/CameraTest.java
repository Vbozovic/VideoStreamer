package app.camera;

import app.threads.VideoReciverTask;
import app.threads.WebcamScanner;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CameraTest {

    @Test
    public void testCameraDetect() throws IOException, InterruptedException {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    ServerSocket sok = new ServerSocket(8080);

                    while(true){
                        Socket sok2 = sok.accept();
                        new Thread(new VideoReciverTask(new ObjectInputStream(sok2.getInputStream()))).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        Socket sok = new Socket("localhost",8080);

        //WebcamScanner vs = new WebcamScanner(new ObjectOutputStream(sok.getOutputStream()));
        //Thread th = new Thread(vs);
//        th.start();

//        th.join();
    }

}
