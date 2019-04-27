package app.error_handling;

public class AzureException extends Exception {

    public String responseBody;
    public int statusCode;

    public AzureException(String message, String responseBody, int statusCode) {
        super(message);
        this.responseBody = responseBody;
        this.statusCode = statusCode;
    }

    public AzureException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return super.toString() + "\n "+responseBody;
    }
}
