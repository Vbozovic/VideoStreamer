package app.image;

import java.awt.image.BufferedImage;

public interface ImageHandler {

    boolean sendImage(BufferedImage img);
    void stop();
}
