package app.image;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;

public class ImageDisplayer implements ImageHandler {

    private ImageView toDisplay;

    public ImageDisplayer(ImageView toDisplay) {
        this.toDisplay = toDisplay;
    }

    @Override
    public boolean sendImage(BufferedImage img) {
        synchronized (toDisplay){
            toDisplay.setImage(SwingFXUtils.toFXImage(img,null));
        }
        return true;
    }

    @Override
    public void stop() {

    }
}
