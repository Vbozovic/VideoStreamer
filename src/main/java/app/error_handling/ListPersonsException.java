package app.error_handling;

public class ListPersonsException extends AzureException {
    public ListPersonsException(String message, String responseBody, int statusCode) {
        super(message, responseBody, statusCode);
    }

    public ListPersonsException(String message, int statusCode) {
        super(message, statusCode);
    }
}
