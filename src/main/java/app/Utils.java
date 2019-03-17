package app;

import app.callback.FaceAttributeCallback;
import app.callback.FaceVectorCallback;
import app.client.FaceClient;
import app.dto.FaceDto;
import app.dto.VectorDto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
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


    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static BufferedImage createImageFromBytes(byte[] pixels, int width, int height) {
        assert height*width*3 == pixels.length;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(pixels, pixels.length), new Point()));
        return img;
    }

}
