package app.dto.azure.recive.group;

public class TrainStatusDto {


    public String status;
    public String createdDateTime;
    public String lastActionDateTime;
    public String message;

    public TrainStatusDto() {
    }

    public TrainStatusDto(String status, String createdDateTime, String lastActionDateTime, String message) {
        this.status = status;
        this.createdDateTime = createdDateTime;
        this.lastActionDateTime = lastActionDateTime;
        this.message = message;
    }
}
