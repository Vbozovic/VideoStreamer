package app.error_handling;

public class GetGroupException extends AzureException {
    public GetGroupException(String message, String responseBody, int statusCode) {
        super(message, responseBody, statusCode);
    }

    public GetGroupException(String message, int statusCode) {
        super(message, statusCode);
    }
}
