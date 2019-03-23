package app.dto.azure.send.group;

public class CreatePersonDto {
    public String name;
    public String userData;

    public CreatePersonDto() {
    }

    public CreatePersonDto(String name, String userData) {
        this.name = name;
        this.userData = userData;
    }
}
