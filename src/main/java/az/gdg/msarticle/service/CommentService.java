package az.gdg.msarticle.service;

import az.gdg.msarticle.model.CommentRequest;

public interface CommentService {
    String deleteComment(String id);

    void postComment(String token, CommentRequest commentRequest);
}
