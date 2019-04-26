package app.image;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ImageSender implements ImageHandler {

    private boolean sentMetaData = false;
    private DataOutputStream output;

    public ImageSender(ObjectOutputStream output) {
        this.output = new DataOutputStream(output);
    }

    @Override
    public void sendImage(BufferedImage img) throws Exception {
        if (!sentMetaData) {
            output.writeChars(img.getHeight()+","+img.getWidth());
            System.out.println("Sent meta data "+img.getHeight()+" "+img.getWidth());
            sentMetaData = true;
        }

        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        output.write(pixels);
        output.flush();
    }
}
