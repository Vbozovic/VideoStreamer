package app.server;

import app.image.ImageDisplayer;
import app.image.ImageSender;
import app.threads.VideoReciverTask;
import app.threads.WebcamScanner;
import com.github.sarxos.webcam.Webcam;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class Server implements Runnable {

    public int port;
    private String host;
    private ExecutorService clientProcessingPool;
    private ImageView display;
    private boolean running;

    private WebcamScanner outSender;
    private VideoReciverTask reciver;
    private Webcam snapshot;

    public Server(int port, ExecutorService pool, ImageView display) {
        this.port = port;
        this.clientProcessingPool = pool;
        this.display = display;
        this.snapshot = Webcam.getDefault();
        this.running = true;
    }


    public WebcamScanner establish_async(String ip) {
        Socket sock = null;
        try {
            sock = new Socket(ip, port);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            DataInputStream in = new DataInputStream(sock.getInputStream());
            this.outSender = new WebcamScanner(new ImageSender(out,snapshot.getFPS()), snapshot);
            this.reciver = new VideoReciverTask(in,new ImageDisplayer(this.display));
            this.clientProcessingPool.execute(this.outSender);
            this.clientProcessingPool.execute(this.reciver);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.outSender;
    }

    public void run() {
        try {
            ServerSocket ssocket = new ServerSocket(port);
            while(running){
                Socket clientSocket = ssocket.accept();

                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());

                this.reciver = new VideoReciverTask(in, new ImageDisplayer(display));
                this.outSender = new WebcamScanner(new ImageSender(out,snapshot.getFPS()),Webcam.getDefault());

                clientProcessingPool.execute(this.reciver);
                clientProcessingPool.execute(this.outSender);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean endCall(){
        if(this.reciver != null && this.outSender != null){
            this.reciver.stop();
            this.outSender.stop();
            return true;
        }
        return false;
    }

    public BufferedImage getCamImg(){
        synchronized (this.snapshot){
            if(this.snapshot.isOpen()){
                return this.snapshot.getImage();
            }else{
                return null;
            }
        }
    }

}

