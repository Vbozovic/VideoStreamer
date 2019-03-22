
package app.dto.azure.recive.list;

import java.util.List;

public class FaceListDto {

    public String faceListId;
    public String name;
    public String userData;
    public List<PersistedFace> persistedFaces = null;

    public FaceListDto() {
    }

    public FaceListDto(String faceListId, String name, String userData, List<PersistedFace> persistedFaces) {
        this.faceListId = faceListId;
        this.name = name;
        this.userData = userData;
        this.persistedFaces = persistedFaces;
    }
}
