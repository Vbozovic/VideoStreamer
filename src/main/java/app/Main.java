package app;

import app.server.Server;

public class Main {

    public static void main(String[] args){

        Server s = new Server(8080,"localhost");

        Thread t = new Thread(s);


        String addr = args[0];
        s.establish(addr);

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
