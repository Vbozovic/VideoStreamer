package app.image;

import org.apache.commons.codec.binary.Base64;
import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Rational;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ImageSender implements ImageHandler {

    private static long segLength = 500; //milisekunde

    private boolean sentMetaData = false;
    private DataOutputStream output;
    private AWTSequenceEncoder encoder;
    private long time = 500;
    private long last;
    private double fps;
    private int currentFrames = 0;
    private SeekableInMemoryByteChannel channel;
    private boolean started = false;

    public ImageSender(DataOutputStream output, double fps) throws IOException {
        this.output = output;
        this.channel = new SeekableInMemoryByteChannel();
        this.encoder = new AWTSequenceEncoder(this.channel, Rational.R(15, 1));
        this.fps = fps;
    }

    @Override
    public void sendImage(BufferedImage img) {
        if(!started){
            this.last = System.currentTimeMillis();
            started = true;
        }

        long current = System.currentTimeMillis();

        try{
            if (current - last >= time) {
                encoder.encodeImage(img);
                encoder.finish();
                System.out.println("Sending video frames "+currentFrames);
                last = current;
                byte[] video = channel.getContents();

                String videoString = Base64.encodeBase64String(video);

                System.out.println("Video len "+video.length);
                output.writeInt(video.length);
                output.writeInt(this.currentFrames);
                output.writeUTF(videoString);
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
