package app.utils;

import app.dto.azure.recive.detect.FaceDetectDto;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;

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

    public static BufferedImage cropImageFaces(FaceDetectDto face, BufferedImage image){
        return image.getSubimage(face.getFaceRectangle().getLeft(),
                face.getFaceRectangle().getTop(),
                face.getFaceRectangle().getWidth()
                ,face.getFaceRectangle().getHeight());
    }


    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static BufferedImage createImageFromBytes(byte[] pixels, int width, int height) {

        if(width*height*3 != pixels.length){
            System.err.println("Array length erro. Expected: "+width*height*3+" got: "+pixels.length);
        }

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(pixels, pixels.length), new Point()));
        return img;
    }

    public static <Controller> void loadAndWaitWindow(String urlPath,int width,int height,Lambda<Controller> setup) throws IOException {
        URI uri = new File(urlPath).toURI();
        FXMLLoader loader = new FXMLLoader(uri.toURL());
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root,width,height));

        Controller cont = loader.<Controller>getController();
        setup.lambda(cont);
        stage.showAndWait();
    }

    public static BufferedImage getBufferedImage(String path) throws InterruptedException {
        final java.awt.Image image = Toolkit.getDefaultToolkit().createImage(path);

        final int[] RGB_MASKS = {0xFF0000, 0xFF00, 0xFF};
        final ColorModel RGB_OPAQUE =
                new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);

        PixelGrabber pg = new PixelGrabber(image, 0, 0, -1, -1, true);
        pg.grabPixels();
        int width = pg.getWidth(), height = pg.getHeight();
        DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), pg.getWidth() * pg.getHeight());
        WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, RGB_MASKS, null);
        return new BufferedImage(RGB_OPAQUE, raster, false, null);
    }

    public static <Controller,T> T loadComponent(String path,Lambda<Controller> setup) throws IOException {
        URI uri = new File(path).toURI();
        FXMLLoader loader = new FXMLLoader(uri.toURL());
        T root = (T)loader.load();
        Controller cont = loader.<Controller>getController();
        setup.lambda(cont);
        return root;
    }

}
