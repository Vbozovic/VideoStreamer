package app.service;

import app.Utils;
import app.client.AzureClient;
import app.dto.azure.recive.detect.FaceDetectDto;
import app.dto.azure.recive.group.*;
import app.dto.azure.recive.list.AddFaceToListResponse;
import app.dto.azure.recive.list.FaceListDto;
import app.dto.azure.recive.list.ListsDto;
import app.dto.azure.send.group.CreateGroupDto;
import app.dto.azure.send.group.CreatePersonDto;
import app.dto.azure.send.list.CreateListDto;
import app.error_handling.AzureException;
import app.error_handling.CreateGroupException;
import app.error_handling.GetGroupException;
import app.error_handling.ListPersonsException;

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

    public static void createGroup(String name,String userData,String groupId) throws CreateGroupException {
        CreateGroupDto dto = new CreateGroupDto(name,userData);
        try {
            AzureClient.put("/persongroups/"+groupId,dto,null,Void.class);
        } catch (AzureException e) {
            throw new CreateGroupException(e.getMessage(),e.responseBody,e.statusCode);
        }
    }

    public static void deleteGroup(String groupId) throws AzureException {
        AzureClient.delete("/persongroups/"+groupId,null,null,Void.class);
    }

    public static GetGroupDto getGroup(String groupId) throws GetGroupException {
        try {
            return AzureClient.get("/persongroups/"+groupId,null,GetGroupDto.class);
        } catch (AzureException e) {
            throw new GetGroupException(e.getMessage(),e.responseBody,e.statusCode);
        }
    }

    public static GetGroupDto[] listGroups() throws AzureException {
        return AzureClient.get("/persongroups",null,GetGroupDto[].class);
    }

    //TODO test
    public static void trainGroup(String groupId) throws AzureException {
        AzureClient.post("/persongroups/"+groupId+"/train",null,null,Void.class);
    }

    //TODO test
    public static TrainStatusDto getGroupTrainStatus(String groupId) throws AzureException {
        return AzureClient.get("/persongroups/"+groupId+"/training",null,TrainStatusDto.class);
    }

    public static PersistedFaceDto addFaceToPerson(BufferedImage face,String personId,String groupId) throws AzureException {
        return AzureClient.post_binary("/persongroups/"+groupId+"/persons/"+personId+"/persistedFaces",
                Utils.imgToBytes(face),null,PersistedFaceDto.class);
    }

    public static CreatedPersonDto createPerson(String personName, String personData, String groupId) throws AzureException {
        CreatePersonDto dto = new CreatePersonDto(personName,personData);
        return AzureClient.post("/persongroups/"+groupId+"/persons",dto,null,CreatedPersonDto.class);
    }

    public static void deletePerson(String personId,String groupId) throws AzureException {
        AzureClient.delete("/persongroups/"+groupId+"/persons/"+personId,null,null,Void.class);
    }

    public static GetPersonDto getPerson(String personId,String groupId) throws AzureException {
        return AzureClient.get("/persongroups/"+groupId+"/persons/"+personId,null,GetPersonDto.class);
    }

    public static GetPersonDto[] listPersons(String groupId) throws ListPersonsException {
        try {
            return AzureClient.get("/persongroups/"+groupId+"/persons",null,GetPersonDto[].class);
        } catch (AzureException e) {
            throw new ListPersonsException(e.getMessage(),e.responseBody,e.statusCode);
        }
    }

}
