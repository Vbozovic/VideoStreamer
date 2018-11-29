package app.server;

import app.threads.ReciverTask;
import app.threads.SenderTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private int port;
    private String host;
    private ExecutorService clientProcessingPool;

    public Server(int port, String host) {
        this.port = port;
        this.host = host;
        this.clientProcessingPool = Executors.newFixedThreadPool(10);
    }

    public void establish(String ip){

        try {
            Socket sock = new Socket(ip,this.port);

            SenderTask st = new SenderTask(sock);
            ReciverTask rt = new ReciverTask(sock,this);

            synchronized (clientProcessingPool) {
                clientProcessingPool.submit(st);
                clientProcessingPool.submit(rt);
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
                clientProcessingPool.submit(new ReciverTask(clientSocket, this));
                clientProcessingPool.submit(new SenderTask(clientSocket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

