package app.client;

import app.dto.CompareDto;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import java.io.*;


public class FaceClient {

    private static String userId = "1076799ded45b143761a";
    private static String userKey = "203b802d44b5002bad28";
    public static String endpoint = "http://facexapi.com";
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

    private static void OkCall(OkHttpClient client,Callback callback, Request request) throws IOException {
        Call call = client.newCall(request);
        call.enqueue(callback);
    }



    public static void postFaceDetect(OkHttpClient client,Callback handler, byte[] picture) throws IOException {

        final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image_attr", "image.jpg", RequestBody.create(MEDIA_TYPE_JPG, picture))
                .build();

        Request request = new Request.Builder()
                .url(endpoint+"/get_image_attr?face_det=1")
                .header("user_id", userId)
                .header("user_key", userKey)
                .post(requestBody)
                .build();

        OkCall(client,handler,request);
    }


    public static void postFaceVector(OkHttpClient client,Callback handler,byte[] picture) throws IOException {
        //http://106.51.58.118:5000/get_face_vec?face_det=0
        final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("img", "image.jpg", RequestBody.create(MEDIA_TYPE_JPG, picture))
                .build();

        Request request = new Request.Builder()
                .url(endpoint+"/get_face_vec?face_det=1")
                .header("user_id", userId)
                .header("user_key", userKey)
                .post(requestBody)
                .build();

        OkCall(client,handler,request);
    }



    public static CompareDto postCompareVectors(OkHttpClient client, byte[] img1, byte[] img2) throws IOException {
        final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("img_1", "image.jpg", RequestBody.create(MEDIA_TYPE_JPG, img1))
                .addFormDataPart("img_2", "image.jpg", RequestBody.create(MEDIA_TYPE_JPG, img2))
                .build();

        Request request = new Request.Builder()
                .url(endpoint+"/compare_faces?face_det=1")
                .header("user_id", userId)
                .header("user_key", userKey)
                .post(requestBody)
                .build();

        Response resp = client.newCall(request).execute();
        JSONObject obj  = new JSONObject(resp.body().string());
        return new CompareDto(obj.getDouble("confidence"));
    }

}
