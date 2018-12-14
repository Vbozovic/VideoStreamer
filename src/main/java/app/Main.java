package app;

import app.server.Server;
import org.opencv.core.Core;

public class Main {

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws InterruptedException {

        Server s = new Server(Integer.parseInt(args[0]),"localhost");
        Thread t = new Thread(s);



        if(args.length > 1){
            String addr = args[1];
            s.establish(addr,Integer.parseInt(args[2]));
        }else{
            t.start();

            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
