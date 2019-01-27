package app.face;

import app.client.FaceClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.asynchttpclient.request.body.multipart.Part;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class RecognitionTest {

    @Test
    public void apiTest() throws IOException {
        Response req = FaceClient.postFaceDetect("faces/jana.jpg");
        System.out.println(req.body().string());
    }

}
