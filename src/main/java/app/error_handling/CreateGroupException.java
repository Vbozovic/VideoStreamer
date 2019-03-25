package app.error_handling;

public class CreateGroupException extends AzureException {
    public CreateGroupException(String message, String responseBody, int statusCode) {
        super(message, responseBody, statusCode);
    }

    public CreateGroupException(String message, int statusCode) {
        super(message, statusCode);
    }
}
