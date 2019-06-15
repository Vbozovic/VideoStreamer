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
    private long last;
    private double fps;
    private int currentFrames = 0;
    private SeekableInMemoryByteChannel channel;

    public ImageSender(ObjectOutputStream output, double fps) throws IOException {
        this.output = output;
        this.channel = new SeekableInMemoryByteChannel();
        this.encoder = new AWTSequenceEncoder(this.channel, Rational.R(15, 1));
        this.fps = fps;
        this.last = System.currentTimeMillis();
    }

    @Override
    public void sendImage(BufferedImage img) {
        long current = System.currentTimeMillis();
        try{
            if (current - last >= time) {
                encoder.encodeImage(img);
                System.out.println("Sending video frames "+currentFrames);
                last = current;
                encoder.finish();
                byte[] video = channel.getContents();
                System.out.println("Video len "+video.length);
                output.writeInt(video.length);
                output.writeInt(this.currentFrames);
                output.writeObject(video);
                output.flush();

                NIOUtils.closeQuietly(this.channel);
                this.channel = new SeekableInMemoryByteChannel();
                this.encoder = new AWTSequenceEncoder(this.channel, Rational.R((int) currentFrames, 1));

                this.currentFrames = 0; //reset broj frejmova
            } else {
                //baferuj sliku
                System.out.print("!");
                this.currentFrames++;
                encoder.encodeImage(img);
            }
        }catch(IOException e){
            System.err.println("Image sender error ");
            e.printStackTrace();
        }


    }
}
