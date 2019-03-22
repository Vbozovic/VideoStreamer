
package app.dto.azure.recive.detect;


public class FaceDetectDto {

    private String faceId;
    private FaceRectangle faceRectangle;
    private FaceAttributes faceAttributes;

    public FaceDetectDto() {
    }

    public FaceDetectDto(String faceId, FaceRectangle faceRectangle, FaceAttributes faceAttributes) {
        this.faceId = faceId;
        this.faceRectangle = faceRectangle;
        this.faceAttributes = faceAttributes;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public FaceRectangle getFaceRectangle() {
        return faceRectangle;
    }

    public void setFaceRectangle(FaceRectangle faceRectangle) {
        this.faceRectangle = faceRectangle;
    }

    public FaceAttributes getFaceAttributes() {
        return faceAttributes;
    }

    public void setFaceAttributes(FaceAttributes faceAttributes) {
        this.faceAttributes = faceAttributes;
    }

}
