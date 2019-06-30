package app.jcodec;

import app.image.SeekableInMemoryByteChannel;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VideoTest {

    @Test
    public void readVideoTest() throws IOException, JCodecException {
        int frameCount = 8;

//        FrameGrab grab = FrameGrab.createFrameGrab(new SeekableInMemoryByteChannel(Files.readAllBytes(Paths.get("resources\\segment0.mp4"))));
        FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(new File("resources\\segment0.mp4")));

        for (int i=0;i<frameCount;i++) {
            Picture picture = grab.getNativeFrame();
            System.out.println(picture.getWidth() + "x" + picture.getHeight() + " " + picture.getColor());
            //for JDK (jcodec-javase)
//            BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
//            ImageIO.write(bufferedImage, "png", new File("frame"+i+".png"));

        }
    }

}
