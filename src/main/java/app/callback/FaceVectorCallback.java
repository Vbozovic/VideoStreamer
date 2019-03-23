package app.callback;

import app.dto.facex.FaceDto;
import app.dto.facex.VectorDto;
import app.gui.service.FaceDisplayService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.Response;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class FaceVectorCallback extends FaceXCallback {

    private FaceDto face;

    public FaceVectorCallback(BufferedImage img, FaceDisplayService service, FaceDto face) {
        super(img, service);
        this.face = face;
    }

    public FaceVectorCallback(BufferedImage img, FaceDisplayService disp) {
        super(img,disp);
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
            face.setVector(dto);
            face.setFaceImage(img);

            synchronized (this.service){
                this.service.addFace(this.img,face);
            }

        }catch(IOException ie) {
            ie.printStackTrace();
        }
    }

}
