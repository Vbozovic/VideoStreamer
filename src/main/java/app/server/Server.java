package app.server;

import app.threads.VideoReciverTask;
import app.threads.VideoSenderTask;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends JFrame implements Runnable, ActionListener {

    public int port;
    private String host;
    private ExecutorService clientProcessingPool;

    private JTextField input;

    public Server(int port, String host) {
        this.port = port;
        this.host = host;
        this.clientProcessingPool = Executors.newFixedThreadPool(20);
        setup();
    }

    private void setup(){
        input = new JTextField(15);
        this.setSize(300,300);

        JPanel pan = new JPanel();
        JLabel lab = new JLabel("IP:");
        JButton ok = new JButton("Connect");

        pan.setLayout(new FlowLayout());
        pan.add(lab);
        pan.add(input);
        pan.add(ok);

        ok.addActionListener(this);

        this.add(pan);

        this.setVisible(true);
        this.pack();
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

    public void actionPerformed(ActionEvent e) {
        establish(input.getText(),this.port);
    }
}

