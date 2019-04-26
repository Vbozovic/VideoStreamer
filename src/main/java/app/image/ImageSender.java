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
            output.flush();
            output.write(img.getWidth());
            output.flush();
            System.out.println("Sent meta data "+img.getHeight()+" "+img.getWidth());
            sentMetaData = true;
        }

        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        output.write(pixels);
        output.flush();
    }
}
