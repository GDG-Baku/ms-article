package az.gdg.msarticle.exception;

public class ArticleNotFound extends RuntimeException {
    public ArticleNotFound(String message) {
        super(message);
    }
}
