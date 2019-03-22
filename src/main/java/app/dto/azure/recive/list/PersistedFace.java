
package app.dto.azure.recive.list;


public class PersistedFace {

    public String persistedFaceId;
    public String userData;

    public PersistedFace(String persistedFaceId, String userData) {
        this.persistedFaceId = persistedFaceId;
        this.userData = userData;
    }
}
