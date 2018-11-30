package app;

import app.server.Server;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Server s = new Server(Integer.parseInt(args[0]),"localhost");

        Thread t = new Thread(s);



        if(args.length > 1){
            String addr = args[1];
            s.establish(addr,Integer.parseInt(args[2]));
        }

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
