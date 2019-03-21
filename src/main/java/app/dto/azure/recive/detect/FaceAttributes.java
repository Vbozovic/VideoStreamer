
package app.dto.azure.recive.detect;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "age",
    "gender"
})
public class FaceAttributes {

    @JsonProperty("age")
    private Float age;
    @JsonProperty("gender")
    private String gender;

    @JsonProperty("age")
    public Float getAge() {
        return age;
    }

    @JsonProperty("age")
    public void setAge(Float age) {
        this.age = age;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

}
