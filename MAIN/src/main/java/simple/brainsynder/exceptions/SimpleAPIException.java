package simple.brainsynder.exceptions;

public class SimpleAPIException extends RuntimeException {

    public SimpleAPIException() {
    }

    public SimpleAPIException(String message) {
        super("[SimpleAPI] Error: " + message);
    }

    public SimpleAPIException(Throwable message) {
        super(message);
    }

    public SimpleAPIException(String message, Throwable cause) {
        super("[SimpleAPI] Error: " + message, cause);
    }

    public SimpleAPIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super("[SimpleAPI] Error: " + message, cause, enableSuppression, writableStackTrace);
    }
}
