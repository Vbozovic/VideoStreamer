package app.server;

import app.threads.VideoReciverTask;
import app.threads.VideoSenderTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    public int port;
    private String host;
    private ExecutorService clientProcessingPool;

    public Server(int port, String host) {
        this.port = port;
        this.host = host;
        this.clientProcessingPool = Executors.newFixedThreadPool(20);
    }

    public void establish(String ip, int port) {

        try {
            Socket sock = new Socket(ip, port);

            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());

            VideoSenderTask st = new VideoSenderTask(this,out);
            VideoReciverTask rt = new VideoReciverTask(this,in);

            synchronized (clientProcessingPool) {
                clientProcessingPool.execute(st);
                clientProcessingPool.execute(rt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        try {
            ServerSocket ssocket = new ServerSocket(port);

            while (true) {
                Socket clientSocket = ssocket.accept();

                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                clientProcessingPool.execute(new VideoReciverTask(this,in));
                clientProcessingPool.execute(new VideoSenderTask(this,out));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

