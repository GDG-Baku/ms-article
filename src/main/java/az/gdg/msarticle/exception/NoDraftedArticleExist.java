package az.gdg.msarticle.exception;

public class NoDraftedArticleExist extends RuntimeException {
    public NoDraftedArticleExist(String message) {
        super(message);
    }
}
