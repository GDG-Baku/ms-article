package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.CommentNotFoundException;
import az.gdg.msarticle.exception.NoAccessException;
import az.gdg.msarticle.model.entity.CommentEntity;
import az.gdg.msarticle.repository.CommentRepository;
import az.gdg.msarticle.service.CommentService;
import az.gdg.msarticle.util.AuthUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentRepository commentRepository;
    private static final String NO_ACCESS_TO_REQUEST = "You don't have access for this request";

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public String deleteComment(String id) {
        logger.info("ServiceLog.deleteComment.start with id {}", id);
        String message;

        String userId = (String) AuthUtil.getAuthenticatedObject().getPrincipal();

        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(() ->
                new CommentNotFoundException("Comment doesn't exist with this id " + id));

        if (commentEntity.getUserId() == Integer.parseInt(userId)) {
            if (commentEntity.getReplies() != null) {
                commentRepository.deleteAll(commentEntity.getReplies());
            }
            commentRepository.deleteById(id);
            logger.info("ServiceLog.deleteComment.success with id {}", id);
            message = "Comment is deleted";
        } else {
            throw new NoAccessException(NO_ACCESS_TO_REQUEST);
        }
        logger.info("ServiceLog.deleteComment.end with id {}", id);
        return message;
    }
}
