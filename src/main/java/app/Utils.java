package app;

import app.dto.azure.recive.detect.FaceDetectDto;
import app.dto.facex.FaceDto;
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
import java.net.URL;

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
        URL url = new File(urlPath).toURL();
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();

        Controller cont = loader.<Controller>getController();
        setup.lambda(cont);

        Stage stage = new Stage();
        stage.setScene(new Scene(root,width,height));
        stage.showAndWait();
    }

}
