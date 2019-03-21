package app.callback;

import app.gui.service.FaceDisplayService;
import okhttp3.Callback;

import java.awt.image.BufferedImage;

public abstract class FaceXCallback implements Callback {

    protected BufferedImage img;
    protected FaceDisplayService service;

    public FaceXCallback(BufferedImage img, FaceDisplayService service) {
        this.img = img;
        this.service = service;
    }
}
