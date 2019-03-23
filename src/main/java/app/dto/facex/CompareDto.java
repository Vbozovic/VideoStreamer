package app.dto.facex;

public class CompareDto {

    private double confidence;

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
