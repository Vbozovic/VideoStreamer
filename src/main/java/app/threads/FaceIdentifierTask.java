package app.threads;

import app.dto.azure.recive.detect.FaceDetectDto;
import app.dto.azure.recive.identify.ConfidenceDto;
import app.dto.azure.recive.identify.IdentifyFaceDto;
import app.dto.azure.send.identify.IdentifyDto;
import app.error_handling.AzureException;
import app.model.MainScreenModel;
import app.service.AzureService;
import app.service.Config;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FaceIdentifierTask implements Runnable {

    private MainScreenModel faces;
    private BufferedImage imageWithFaces;

    public FaceIdentifierTask(MainScreenModel faces, BufferedImage imageWithFaces) {
        this.faces = faces;
        this.imageWithFaces = imageWithFaces;
    }

    @Override
    public void run() {
        try {
            FaceDetectDto[] faces = AzureService.detectFaces(imageWithFaces);
            List<String> ids = Arrays.stream(faces).map(FaceDetectDto::getFaceId).collect(Collectors.toList());
            IdentifyFaceDto[] persons = AzureService.identifyFace(Config.getInstance().group_id,ids.toArray(new String[ids.size()]));

            Arrays.stream(persons).map(pdto->pdto.candidates).forEach(cand->{
                String person = this.faces.getPersonById(cand[0].personId);
                if (person!= null){
                    System.out.println("Person found: "+person+" confidence: "+cand[0].confidence);
                }
            });


        } catch (AzureException e) {
            e.printStackTrace();
        }
    }
}
