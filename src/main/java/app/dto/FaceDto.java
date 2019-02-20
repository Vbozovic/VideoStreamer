package app.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;

public class FaceDto {

    private static int ULX = 0;
    private static int ULY = 1;
    private static int LRX = 2;
    private static int LRY = 3;

    private String age;
    private int[] faceRectangle;
    private String gender;
    private float genderConfidence;

    @JsonIgnore
    private VectorDto vector;



    public FaceDto() {
    }

    public FaceDto(String age, int[] faceRectangle, String gender, float genderConfidence) {
        this.age = age;
        this.faceRectangle = faceRectangle;
        this.gender = gender;
        this.genderConfidence = genderConfidence;
    }

    public VectorDto getVector() {
        return vector;
    }

    public void setVector(VectorDto vector) {
        this.vector = vector;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int[] getFaceRectangle() {
        return faceRectangle;
    }

    public void setFaceRectangle(int[] faceRectangle) {
        this.faceRectangle = faceRectangle;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getConfidence() {
        return genderConfidence;
    }

    public void setGenderConfidence(float genderConfidence) {
        this.genderConfidence = genderConfidence;
    }

    public int ULX() {
        return this.faceRectangle[ULX];
    }

    public int ULY() {
        return this.faceRectangle[ULY];
    }

    public int LRX() {
        return this.faceRectangle[LRX];
    }

    public int LRY() {
        return this.faceRectangle[LRY];
    }

    @Override
    public String toString() {
        return "FaceDto{" +
                "age=" + age +
                ", faceRectangle=" + Arrays.toString(faceRectangle) +
                ", gender='" + gender + '\'' +
                ", genderConfidence=" + genderConfidence +
                '}';
    }
}
