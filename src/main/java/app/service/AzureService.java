package app.service;

import app.Utils;
import app.client.AzureClient;
import app.dto.azure.recive.list.AddFaceToListResponse;
import app.dto.azure.recive.detect.FaceDetectDto;
import app.dto.azure.recive.list.FaceListDto;
import app.dto.azure.send.CreateListDto;
import app.error_handling.AzureException;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class AzureService {

    public static FaceDetectDto[] detectFaces(BufferedImage img) throws AzureException {
        HashMap<String,String> params = new HashMap<>();
        params.put("returnFaceLandmarks","false");
        params.put("returnFaceId","true");
        params.put("returnFaceAttributes","age,gender");
        return AzureClient.post_binary("/detect", Utils.imgToBytes(img),params,FaceDetectDto[].class);
    }

    //TODO test
    public static void createList(String name, String listId, String userData) throws AzureException {

        CreateListDto dto = new CreateListDto(name,userData);
        AzureClient.post("/facelists/"+listId,dto,null,CreateListDto.class);
    }

    //TODO test
    public static AddFaceToListResponse addFaceToList(BufferedImage faceImage,String faceListid,String userData) throws AzureException {

        HashMap<String, String> params = new HashMap<>();
        params.put("userData",userData);

        AddFaceToListResponse resp = AzureClient.post_binary("facelists/"+faceListid+"/persistedFaces",
                Utils.imgToBytes(faceImage),params,AddFaceToListResponse.class);
        return resp;
    }

    //TODO Test
    public static FaceListDto getListOfFaces(String faceListid) throws AzureException {
        return AzureClient.get("/facelists/"+faceListid,null,FaceListDto.class);
    }

}
