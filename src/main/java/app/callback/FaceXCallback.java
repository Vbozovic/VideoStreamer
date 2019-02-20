package app.callback;

import okhttp3.Callback;

import java.awt.image.BufferedImage;

public abstract class FaceXCallback implements Callback {

    protected BufferedImage img;

    public FaceXCallback(BufferedImage img) {
        this.img = img;
    }
}
