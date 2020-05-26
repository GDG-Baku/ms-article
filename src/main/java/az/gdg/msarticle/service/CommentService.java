package az.gdg.msarticle.service;

import az.gdg.msarticle.model.CommentRequest;

public interface CommentService {

    void postComment(String token, CommentRequest commentRequest);

}
