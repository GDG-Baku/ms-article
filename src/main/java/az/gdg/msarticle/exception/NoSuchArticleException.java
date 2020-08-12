package az.gdg.msarticle.exception;

public class NoSuchArticleException extends RuntimeException {
    public NoSuchArticleException(String message) {
        super(message);
    }
}
