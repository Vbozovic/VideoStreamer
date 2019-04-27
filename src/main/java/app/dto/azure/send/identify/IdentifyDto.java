package app.dto.azure.send.identify;

public class IdentifyDto {
    public String personGroupId;
    public String[] faceIds;
    public int maxNumOfCandidatesReturned;

    public IdentifyDto(String[] faceIds, String personGroupId,int maxNumOfCandidatesReturned) {
        this.faceIds = faceIds;
        this.personGroupId = personGroupId;
        this.maxNumOfCandidatesReturned = maxNumOfCandidatesReturned;
    }
    public IdentifyDto(){

    }
}
