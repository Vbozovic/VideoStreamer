package app.image;

import java.awt.image.BufferedImage;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ImageSender implements ImageHandler {

    private ObjectOutputStream output;
    private String address;
    private Socket other;

    public ImageSender(String address) {
        this.address = address;
    }

    @Override
    public void sendImage(BufferedImage img) {

    }
}
