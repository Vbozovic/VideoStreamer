package app.image;

import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Rational;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ImageSender implements ImageHandler {

    private static long segLength = 500; //milisekunde

    private boolean sentMetaData = false;
    private DataOutputStream output;

    private int fileIndex = 0;
    private long lastSegment = 0;
    private AWTSequenceEncoder encoder;

    public ImageSender(ObjectOutputStream output) throws IOException {
        this.encoder = new AWTSequenceEncoder(new SocketByteChannelSender(new DataOutputStream(output)), Rational.ONE);
        this.output = new DataOutputStream(output);
    }

    @Override
    public void sendImage(BufferedImage img) throws Exception {
        /*if (!sentMetaData) {
            output.writeUTF(img.getHeight()+","+img.getWidth()+",");
            output.flush();
            System.out.println("Sent meta data "+img.getHeight()+" "+img.getWidth());
            sentMetaData = true;
        }*/

        this.encoder.encodeImage(img);
//        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
//        output.write(pixels);
//        output.flush();
    }
}
