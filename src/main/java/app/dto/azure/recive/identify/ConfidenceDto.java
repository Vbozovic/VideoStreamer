package app.dto.azure.recive.identify;

public class ConfidenceDto {
    public String personId;
    public float confidence;

    public ConfidenceDto(String personId, float confidence) {
        this.personId = personId;
        this.confidence = confidence;
    }

    public ConfidenceDto() {
    }
}
