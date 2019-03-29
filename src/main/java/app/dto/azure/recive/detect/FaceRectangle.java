
package app.dto.azure.recive.detect;

public class FaceRectangle {

    private Integer width;
    private Integer height;
    private Integer left;
    private Integer top;

    public FaceRectangle() {
    }

    public FaceRectangle(Integer width, Integer height, Integer left, Integer top) {
        this.width = width;
        this.height = height;
        this.left = left;
        this.top = top;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

}
