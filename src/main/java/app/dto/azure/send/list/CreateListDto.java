package app.dto.azure.send.list;

public class CreateListDto {
    public String name;
    public String userData;

    public CreateListDto(String name, String userData) {
        this.name = name;
        this.userData = userData;
    }
}
