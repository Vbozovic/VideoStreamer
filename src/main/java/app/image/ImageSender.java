package app.image;

import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Rational;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ImageSender implements ImageHandler {

    private static long segLength = 500; //milisekunde

    private boolean sentMetaData = false;
    private ObjectOutputStream output;
    private AWTSequenceEncoder encoder;
    private long time = 500;
    private long last = Long.MAX_VALUE;
    private ArrayByteChannel channel;
    private double fps;

    public ImageSender(ObjectOutputStream output, double fps) throws IOException {
        this.output = output;
        this.channel = new ArrayByteChannel();
        this.encoder = new AWTSequenceEncoder(this.channel, Rational.R((int) this.fps, 1));
    }

    @Override
    public void sendImage(BufferedImage img) throws Exception {
        long current = System.currentTimeMillis();
        if (last - current >= time) {
            //posalji sliku kroz stream
            last = current;
            encoder.finish();
            byte[] video = channel.getOut().toByteArray();
            output.writeObject(video);

            NIOUtils.closeQuietly(this.channel);
            this.channel = new ArrayByteChannel();
            this.encoder = new AWTSequenceEncoder(this.channel, Rational.R((int) this.fps, 1));
        } else {
            //baferuj sliku
            encoder.encodeImage(img);
        }
    }
}
