package app.callback;

import okhttp3.Call;
import okhttp3.Response;

import java.io.IOException;

public class FaceAttributeCallback extends FaceXCallback {
    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String body = response.body().string();
        //For every unique face in the sent picture, there will be a Json OBJECT with name face_id_*

    }
}
