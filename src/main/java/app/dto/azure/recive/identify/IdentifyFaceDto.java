package app.dto.azure.recive.identify;

public class IdentifyFaceDto {
    public  String faceId;
    public ConfidenceDto[] candidates;

    public IdentifyFaceDto(String faceId, ConfidenceDto[] candidates) {
        this.faceId = faceId;
        this.candidates = candidates;
    }

    public IdentifyFaceDto() {
    }
}
