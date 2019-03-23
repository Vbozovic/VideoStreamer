package app.service;

import app.Utils;
import app.client.AzureClient;
import app.dto.azure.recive.group.GetGroupDto;
import app.dto.azure.recive.group.TrainStatusDto;
import app.dto.azure.recive.list.AddFaceToListResponse;
import app.dto.azure.recive.detect.FaceDetectDto;
import app.dto.azure.recive.list.FaceListDto;
import app.dto.azure.recive.list.ListsDto;
import app.dto.azure.send.group.CreateGroupDto;
import app.dto.azure.send.list.CreateListDto;
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

    public static void createList(String name, String listId, String userData) throws AzureException {

        CreateListDto dto = new CreateListDto(name,userData);
        AzureClient.put("/facelists/"+listId,dto,null,CreateListDto.class);
    }

    //TODO test
    public static AddFaceToListResponse addFaceToList(BufferedImage faceImage,String faceListid,String userData) throws AzureException {

        HashMap<String, String> params = new HashMap<>();
        params.put("userData",userData);

        AddFaceToListResponse resp = AzureClient.post_binary("facelists/"+faceListid+"/persistedFaces",
                Utils.imgToBytes(faceImage),params,AddFaceToListResponse.class);
        return resp;
    }

    public static FaceListDto getListOfFaces(String faceListid) throws AzureException {
        return AzureClient.get("/facelists/"+faceListid,null,FaceListDto.class);
    }

    public static ListsDto[] getLists() throws AzureException {
        return AzureClient.get("/facelists",null, ListsDto[].class);
    }


    public static void deleteFaceList(String faceListid) throws AzureException {
        AzureClient.delete("/facelists/"+faceListid,null,null,Void.class);
    }

    public static void createGroup(String name,String userData,String groupId) throws AzureException {
        CreateGroupDto dto = new CreateGroupDto(name,userData);
        AzureClient.put("/persongroups/"+groupId,dto,null,Void.class);
    }

    public static void deleteGroup(String groupId) throws AzureException {
        AzureClient.delete("/persongroups/"+groupId,null,null,Void.class);
    }

    public static GetGroupDto getGroup(String groupId) throws AzureException {
       return AzureClient.get("/persongroups/"+groupId,null,GetGroupDto.class);
    }

    public static GetGroupDto[] listGroups() throws AzureException {
        return AzureClient.get("/persongroups",null,GetGroupDto[].class);
    }

    public static void trainGroup(String groupId) throws AzureException {
        AzureClient.post("/persongroups/"+groupId+"/train",null,null,Void.class);
    }

    public static TrainStatusDto getGroupTrainStatus(String groupId) throws AzureException {
        return AzureClient.get("/persongroups/"+groupId+"/training",null,TrainStatusDto.class);
    }

}
