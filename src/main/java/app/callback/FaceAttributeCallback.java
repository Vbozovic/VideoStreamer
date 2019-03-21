package app.callback;

import app.Utils;
import app.client.FaceClient;
import app.dto.FaceDto;
import app.gui.service.FaceDisplayService;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

public class FaceAttributeCallback extends FaceXCallback {

    public FaceAttributeCallback(FaceDisplayService service, BufferedImage img) {
        super(img,service);
        this.service = service;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        System.err.println("Fail FaceAttributeCallback");
    }


    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String body = response.body().string();
        //For every unique face in the sent picture, there will be a Json OBJECT with name face_id_*
        int faces = StringUtils.countMatches(body,"face_id");
        JSONObject responseJson = new JSONObject(body);
        LinkedList<FaceDto> facesList = new LinkedList<>();
        for (int i = 0; i < faces; i++) {
            FaceDto face = fromJson(responseJson.getJSONObject("face_id_"+i));
            face.expandFaceBox(0.15f,this.img.getHeight(),this.img.getWidth());
            facesList.add(face);
        }

        for (FaceDto face : facesList){
            //for every face we crop the image and send that to vector
            FaceClient.postFaceVector(new OkHttpClient(),new FaceVectorCallback(this.img,this.service,face), Utils.imgToBytes(Utils.cropImageFaces(face,this.img)));
        }

        //this.service.setFaces(this.img,facesList);

    }


    private FaceDto fromJson(JSONObject dto){
        FaceDto face = new FaceDto();

        face.setAge(dto.getString("age"));
        face.setGender(dto.getString("gender"));
        face.setGenderConfidence(dto.getFloat("gender_confidence"));
        JSONArray rectangleArray = dto.getJSONArray("face_rectangle");

        int[] arr = new int[4];
        for (int i = 0; i < 4; i++) {
            arr[i] = rectangleArray.getInt(i);
        }

        face.setFaceRectangle(arr);
        return face;
    }

}
