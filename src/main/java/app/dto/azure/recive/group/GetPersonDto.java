package app.dto.azure.recive.group;

import java.util.List;

public class GetPersonDto {

    public String personId;
    public List<String> persistedFaceIds = null;
    public String name;
    public String userData;

    public GetPersonDto() {
    }

    public GetPersonDto(String personId, List<String> persistedFaceIds, String name, String userData) {
        this.personId = personId;
        this.persistedFaceIds = persistedFaceIds;
        this.name = name;
        this.userData = userData;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
