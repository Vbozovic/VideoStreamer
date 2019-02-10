package app.dto;

public class FaceDto {

    private static int ULX = 0;
    private static int ULY = 1;
    private static int LRX = 2;
    private static int LRY = 3;

    private int age;
    private int[] face_rectangle;
    private String gender;
    private float gender_confidence;

    public FaceDto() {
    }

    public FaceDto(int age, int[] face_rectangle, String gender, float gender_confidence) {
        this.age = age;
        this.face_rectangle = face_rectangle;
        this.gender = gender;
        this.gender_confidence = gender_confidence;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int[] getFace_rectangle() {
        return face_rectangle;
    }

    public void setFace_rectangle(int[] face_rectangle) {
        this.face_rectangle = face_rectangle;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getGender_confidence() {
        return gender_confidence;
    }

    public void setGender_confidence(float gender_confidence) {
        this.gender_confidence = gender_confidence;
    }

    public int ULX(){
        return this.face_rectangle[ULX];
    }

    public int ULY(){
        return this.face_rectangle[ULY];
    }

    public int LRX(){
        return this.face_rectangle[LRX];
    }

    public int LRY(){
        return this.face_rectangle[LRY];
    }
}
