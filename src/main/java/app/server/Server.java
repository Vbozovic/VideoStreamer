package app.server;

import app.threads.ReciverTask;
import app.threads.SenderTask;

import java.io.IOException;
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
            SenderTask st = new SenderTask(sock);
            ReciverTask rt = new ReciverTask(sock, this);

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
                clientProcessingPool.execute(new ReciverTask(clientSocket, this));
                clientProcessingPool.execute(new SenderTask(clientSocket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

