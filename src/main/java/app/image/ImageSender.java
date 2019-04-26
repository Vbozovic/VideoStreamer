package app.image;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ImageSender implements ImageHandler {

    private ObjectOutputStream output;
    private boolean sentMetaData = false;

    public ImageSender(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public void sendImage(BufferedImage img) throws Exception {
        if (!sentMetaData) {
            output.writeInt(img.getHeight());
            output.write(img.getWidth());
            System.out.println("Sent meta data");
            sentMetaData = true;
        }

        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        output.write(pixels);
        output.flush();
    }
}
