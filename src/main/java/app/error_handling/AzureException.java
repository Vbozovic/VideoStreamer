package app.error_handling;

public class AzureException extends Exception {

    private String responseBody;
    private int statusCode;

    public AzureException(String message, String responseBody, int statusCode) {
        super(message);
        this.responseBody = responseBody;
        this.statusCode = statusCode;
    }

    public AzureException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
