package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.ArticleNotFoundException;
import az.gdg.msarticle.exception.CommentNotFoundException;
import az.gdg.msarticle.exception.InvalidTokenException;
import az.gdg.msarticle.exception.NoAccessException;
import az.gdg.msarticle.exception.NotAllowedException;
import az.gdg.msarticle.mapper.CommentMapper;
import az.gdg.msarticle.model.CommentRequest;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.model.entity.CommentEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.repository.CommentRepository;
import az.gdg.msarticle.service.CommentService;
import az.gdg.msarticle.util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private static final String NO_ACCESS_TO_REQUEST = "You don't have access for this request";

    public CommentServiceImpl(CommentRepository commentRepository,
                              ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    private Authentication getAuthenticatedObject() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new InvalidTokenException("Token is not valid or it is expired");
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }


    @Override
    public void postComment(String token, CommentRequest commentRequest) {
        logger.info("ActionLog.postComment.start : articleId {}", commentRequest.getArticleId());

        ArticleEntity article = articleRepository.findById(commentRequest.getArticleId()).orElseThrow(
                () -> new ArticleNotFoundException("Not found such article!")
        );

        String userId = (String) getAuthenticatedObject().getPrincipal();
        CommentEntity commentEntity = CommentMapper.INSTANCE.requestToEntity(commentRequest);
        commentEntity.setUserId(Long.parseLong(userId));

        if (!commentRequest.getParentCommentId().isEmpty()) {
            commentEntity.setReply(true);
            CommentEntity comment = commentRepository.findById(commentRequest.getParentCommentId())
                    .orElseThrow(() -> new CommentNotFoundException("Not found such comment!"));


            if (!comment.isReply()) {
                comment.getReplies().add(commentEntity);
                commentRepository.save(commentEntity);
                commentRepository.save(comment);

            } else {
                throw new NotAllowedException("Is not allowed writing reply to reply");
            }


        } else {
            commentEntity.setReplies(Collections.emptyList());
            commentEntity.setReply(false);
            commentRepository.save(commentEntity);
            article.getComments().add(commentEntity);
            articleRepository.save(article);
        }


        logger.info("ActionLog.postComment.stop.success : articleId {}", commentRequest.getArticleId());

    }


    @Override
    public String deleteComment(String id) {
        logger.info("ActionLog.deleteComment.start with id {}", id);
        String message;

        String userId = (String) AuthUtil.getAuthenticatedObject().getPrincipal();

        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(() ->
                new CommentNotFoundException("Comment doesn't exist with this id " + id));

        if (commentEntity.getUserId() == Integer.parseInt(userId)) {
            if (commentEntity.getReplies() != null) {
                commentRepository.deleteAll(commentEntity.getReplies());
            }
            commentRepository.deleteById(id);
            logger.info("ActionLog.deleteComment.success with id {}", id);
            message = "Comment is deleted";
        } else {
            throw new NoAccessException(NO_ACCESS_TO_REQUEST);
        }
        logger.info("ActionLog.deleteComment.end with id {}", id);
        return message;
    }
}
