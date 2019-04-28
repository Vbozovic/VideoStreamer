package app.image;

import javax.imageio.ImageIO;
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
            output.writeUTF(img.getHeight()+","+img.getWidth()+",");
            output.flush();
            System.out.println("Sent meta data "+img.getHeight()+" "+img.getWidth());
            sentMetaData = true;
        }


        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        ImageIO.write(img,"PNG",output);
//        output.write(pixels);
//        output.flush();
    }
}
