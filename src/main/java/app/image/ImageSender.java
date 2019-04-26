package app.image;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ImageSender implements ImageHandler {

    private ObjectOutputStream output;

    public ImageSender(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public void sendImage(BufferedImage img) {
        byte[] pixels = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
        try {
            output.write(pixels);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
