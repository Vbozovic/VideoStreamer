package app.dto;

public class CompareDto {

    private Double confidence;

    public CompareDto(double confidence) {
        this.confidence = confidence;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}
