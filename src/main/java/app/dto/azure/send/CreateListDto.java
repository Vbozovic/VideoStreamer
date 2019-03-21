package app.dto.azure.send;

public class CreateListDto {
    public String name;
    public String userData;

    public CreateListDto(String name, String userData) {
        this.name = name;
        this.userData = userData;
    }
}
