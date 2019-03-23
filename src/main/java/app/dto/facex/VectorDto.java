package app.dto.facex;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonRootName(value = "vector")
public class VectorDto implements Serializable {

    @JsonProperty(value = "__ndarray__")
    public ArrayList<Double> ndarray = null;
    public String dtype;
    public List<Integer> shape = null;

    public VectorDto(ArrayList<Double> ndarray, String dtype, List<Integer> shape) {
        this.ndarray = ndarray;
        this.dtype = dtype;
        this.shape = shape;
    }

    public VectorDto() {
    }

    public List<Double> getNdarray() {
        return ndarray;
    }

    public void setNdarray(ArrayList<Double> ndarray) {
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

    public double euclidianDistance(VectorDto other){
        double sum = 0;
        for (int i = 0; i < this.ndarray.size(); i++) {
            sum+= Math.pow((this.ndarray.get(i) - other.ndarray.get(i)),2.0);
        }
        return Math.sqrt(sum);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof VectorDto){
            VectorDto vec = (VectorDto) obj;

            for (int i = 0; i < vec.ndarray.size(); i++) {
                if(vec.ndarray.get(i) != this.ndarray.get(i)){
                    return false;
                }
            }
            return true;
        }else{
            return false;
        }
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
