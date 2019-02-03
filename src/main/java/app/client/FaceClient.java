package app.client;

import okhttp3.*;
import org.apache.commons.io.FileUtils;


import java.io.*;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;


public class FaceClient {

    private static String userId = "1076799ded45b143761a";
    private static String userKey = "203b802d44b5002bad28";
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");


    public static byte[] encodeFileToBinary(File file) {

        try {
            byte[] fileContent = FileUtils.readFileToByteArray(file);
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    public static void postFaceDetect(byte[] picture) throws IOException {

        final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image_attr", "image.jpg", RequestBody.create(MEDIA_TYPE_JPG, picture))
                .build();

        Request request = new Request.Builder()
                .url("http://106.51.58.118:5000/get_image_attr?face_det=1")
                .header("user_id", userId)
                .header("user_key", userKey)
                .post(requestBody)
                .build();

        OkCall(request);
    }


    public static void postFaceVector(byte[] picture) throws IOException {
        //http://106.51.58.118:5000/get_face_vec?face_det=0
        final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("img", "image.jpg", RequestBody.create(MEDIA_TYPE_JPG, picture))
                .build();

        Request request = new Request.Builder()
                .url("http://106.51.58.118:5000/get_face_vec?face_det=1")
                .header("user_id", userId)
                .header("user_key", userKey)
                .post(requestBody)
                .build();

        OkCall(request);
    }

    private static void OkCall(Request request) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        String res = response.body().string();
        System.out.println(res);
        response.close();
    }

    public static void postCompareVectors(byte[] img1, byte[] img2) throws IOException {
        final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("img_1", "image.jpg", RequestBody.create(MEDIA_TYPE_JPG, img1))
                .addFormDataPart("img_2", "image.jpg", RequestBody.create(MEDIA_TYPE_JPG, img2))
                .build();

        Request request = new Request.Builder()
                .url("http://106.51.58.118:5000/compare_face?face_det=1")
                .header("user_id", userId)
                .header("user_key", userKey)
                .post(requestBody)
                .build();

        OkCall(request);
    }

}
