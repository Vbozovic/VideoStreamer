package app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName(value = "vector")
public class VectorDto {

    @JsonProperty(value = "__ndarray__")
    public List<Double> ndarray = null;
    public String dtype;
    public List<Integer> shape = null;

    public VectorDto(List<Double> ndarray, String dtype, List<Integer> shape) {
        this.ndarray = ndarray;
        this.dtype = dtype;
        this.shape = shape;
    }

    public VectorDto() {
    }

    public List<Double> getNdarray() {
        return ndarray;
    }

    public void setNdarray(List<Double> ndarray) {
        this.ndarray = ndarray;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public List<Integer> getShape() {
        return shape;
    }

    public void setShape(List<Integer> shape) {
        this.shape = shape;
    }


    @Override
    public String toString() {
        return "VectorDto{" +
                "ndarray=" + ndarray +
                ", dtype='" + dtype + '\'' +
                ", shape=" + shape +
                '}';
    }
}
