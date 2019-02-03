package app.face;

import app.client.FaceClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.asynchttpclient.request.body.multipart.Part;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class RecognitionTest {

    @Test
    public void apiTest() throws IOException {
        //Response req = FaceClient.postFaceDetect("faces/jana.jpg");
        //System.out.println(req.body().string());

//        FaceClient.postFaceDetect(FaceClient.encodeFileToBinary(new File("faces/jana.jpg")));
//        FaceClient.postFaceVector(FaceClient.encodeFileToBinary(new File("faces/jana.jpg")));
//        FaceClient.postCompareVectors(FaceClient.encodeFileToBinary(new File("faces/ja1.jpg")),FaceClient.encodeFileToBinary(new File("faces/ja2.jpg")));
    }
}
