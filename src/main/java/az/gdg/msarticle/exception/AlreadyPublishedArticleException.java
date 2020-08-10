package az.gdg.msarticle.exception;

public class AlreadyPublishedArticleException extends RuntimeException {
    public AlreadyPublishedArticleException(String message) {
        super(message);
    }
}
