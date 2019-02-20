package app.callback;

import app.dto.VectorDto;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.Response;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class FaceVectorCallback extends FaceXCallback {


    public FaceVectorCallback(BufferedImage img) {
        super(img);
    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String body = response.body().string();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);

        try{
            VectorDto dto = objectMapper.readValue(body, VectorDto.class);
            System.out.println(dto.toString());

        }catch(IOException ie) {
            ie.printStackTrace();
        }
    }

}
