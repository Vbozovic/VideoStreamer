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
    private AWTSequenceEncoder encoder;

    public ImageSender(ObjectOutputStream output) throws IOException {
        this.output = new DataOutputStream(output);
        this.encoder = new AWTSequenceEncoder(new SocketByteChannelSender(this.output), Rational.R(30,1));
    }

    @Override
    public void sendImage(BufferedImage img) throws Exception {
        this.encoder.encodeImage(img);
    }
}
