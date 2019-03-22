package app.dto.azure.send.group;

public class CreateGroupDto {

    public String name;
    public String userData;

    public CreateGroupDto() {
    }

    public CreateGroupDto(String name, String userData) {
        this.name = name;
        this.userData = userData;
    }
}
