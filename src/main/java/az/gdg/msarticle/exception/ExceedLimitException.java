package az.gdg.msarticle.exception;

public class ExceedLimitException extends RuntimeException {
    public ExceedLimitException(String message) {
        super(message);
    }
}
