package app.dto.azure.recive.list;

public class ListsDto {

    public String faceListId;
    public String name;
    public String userData;

    public ListsDto() {
    }

    public ListsDto(String faceListId, String name, String userData) {
        this.faceListId = faceListId;
        this.name = name;
        this.userData = userData;
    }
}
