package app.service;

import app.dto.azure.recive.detect.FaceAttributes;
import app.dto.azure.recive.detect.FaceDetectDto;
import app.error_handling.AzureException;
import app.utils.Utils;
import javafx.util.Pair;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class FaceSeparatorService {

    public BufferedImage imageToSeperate;
    private ArrayList<BufferedImage> faces;
    private ArrayList<Pair<BufferedImage, FaceAttributes>> orderedFaces;

    public FaceSeparatorService(BufferedImage imageToSeperate) {
        this.imageToSeperate = imageToSeperate;
        this.faces = new ArrayList<>();
        orderedFaces = new ArrayList<>();
    }

    public ArrayList<BufferedImage> separate(BufferedImage imageToSeperate){
        try {
            FaceDetectDto[] detectedFaces = AzureService.detectFaces(imageToSeperate);
            for (FaceDetectDto singleFace : detectedFaces){
                BufferedImage img = Utils.cropImageFaces(singleFace,imageToSeperate);
                faces.add(img);
                orderedFaces.add(new Pair<>(img,singleFace.getFaceAttributes()));
            }
        } catch (AzureException e) {
            e.printStackTrace();
        }
        return faces;
    }

    public ArrayList<Pair<BufferedImage, FaceAttributes>> getOrderedFaces() {
        return orderedFaces;
    }
}
