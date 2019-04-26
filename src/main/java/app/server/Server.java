package app.server;

import app.image.ImageDisplayer;
import app.image.ImageSender;
import app.threads.VideoReciverTask;
import app.threads.WebcamScanner;
import com.github.sarxos.webcam.Webcam;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class Server implements Runnable {

    public int port;
    private String host;
    private ExecutorService clientProcessingPool;
    private ImageView display;

    public Server(int port, ExecutorService pool, ImageView display) {
        this.port = port;
        this.clientProcessingPool = pool;
        this.display = display;
    }


    public WebcamScanner establish_async(String ip) {
        Socket sock = null;
        WebcamScanner st = null;
        try {
            sock = new Socket(ip, port);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            st = new WebcamScanner(new ImageSender(out), Webcam.getDefault());
            VideoReciverTask rt = new VideoReciverTask(in,this.display);
            this.clientProcessingPool.execute(rt);
            this.clientProcessingPool.execute(st);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return st;
    }

    public void run() {
        try {
            ServerSocket ssocket = new ServerSocket(port);

            Socket clientSocket = ssocket.accept();

            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            clientProcessingPool.execute(new VideoReciverTask(in, display));
            clientProcessingPool.execute(new WebcamScanner(new ImageSender(out),Webcam.getDefault()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

