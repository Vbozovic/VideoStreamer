package app.dto.azure.recive.group;

public class GetGroupDto {

    public String personGroupId;
    public String name;
    public String userData;

    public GetGroupDto() {
    }

    public GetGroupDto(String personGroupId, String name, String userData) {
        this.personGroupId = personGroupId;
        this.name = name;
        this.userData = userData;
    }
}
