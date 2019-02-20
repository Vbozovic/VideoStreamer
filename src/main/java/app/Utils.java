package app;

import app.callback.FaceAttributeCallback;
import app.client.FaceClient;
import app.dto.FaceDto;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Utils {

    public static byte[] imgToBytes(BufferedImage image){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage cropImageFaces(FaceDto face,BufferedImage image){
        int UX,UY,LX,LY;
        UX = face.ULX();
        UY = face.ULY();
        LX = face.LRX();
        LY = face.LRY();
        return image.getSubimage(UX,UY,LX-UX,LY-UY);
    }

}
