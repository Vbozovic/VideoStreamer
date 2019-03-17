package app.dto;

public class FaceXCompareDto implements Into<CompareDto>{

    public String confidence;

    public FaceXCompareDto(String confidence) {
        this.confidence = confidence;
    }

    @Override
    public CompareDto into() {
        return new CompareDto(Double.parseDouble(confidence));
    }
}
