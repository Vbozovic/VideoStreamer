package app.dto.azure.recive.list;

public class AddFaceToListResponse {
    public String persistedFaceId;

    public AddFaceToListResponse(String persistedFaceId) {
        this.persistedFaceId = persistedFaceId;
    }
}
