package app.image;

import java.awt.image.BufferedImage;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ImageSender implements ImageHandler {

    private ObjectOutputStream output;

    public ImageSender(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public void sendImage(BufferedImage img) {

    }
}
